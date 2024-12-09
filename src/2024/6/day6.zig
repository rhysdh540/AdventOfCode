const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;
const string = []const u8;

const day: u16 = 6;

pub fn part1(input: string) !usize {
    return (try getVisitedPoints(try parseInput(input))).table.count();
}

pub fn part2(input: string) !usize {
    const parsed = try parseInput(input);
    const grid = parsed.grid;
    const start = parsed.start;

    var count: usize = 0;

    var itrMutex: std.Thread.Mutex = .{};
    var countMutex: std.Thread.Mutex = .{};
    const numThreads = (std.Thread.getCpuCount() catch 2) / 2;
    var threads = try allocator.alloc(std.Thread, numThreads);

    // Only try to place obstacles where the guard will move, anywhere else will not affect the result
    var itr = (try getVisitedPoints(parsed)).table.keyIterator();
    for(0..numThreads) |i| {
        const thread = try std.Thread.spawn(.{ .allocator = allocator },
            part2_thread, .{ grid, start, &itr, &count, &itrMutex, &countMutex });
        threads[i] = thread;
    }

    for(threads) |thread| {
        thread.join();
    }

    return count;
}

fn part2_thread(
    grid: [][]u8, start: Point,
    itr: *std.AutoHashMap(Point, bool).KeyIterator,
    count: *usize,
    itrMutex: *std.Thread.Mutex, countMutex: *std.Thread.Mutex
) !void {
    var point: *Point = undefined;

    while(true) {
        // synchronize on the iterator to prevent getting the same point multiple times
        itrMutex.lock();
        point = itr.next() orelse {
            itrMutex.unlock();
            break;
        };
        itrMutex.unlock();

        // we can run isLoop in parallel since it doesn't modify the grid (just the checks)
        if(try isLoop(grid, start, point.*)) {

            // synchronize on the count to make sure we don't miss any increments
            countMutex.lock();
            count.* += 1;
            countMutex.unlock();
        }
    }
}

fn getVisitedPoints(input: ParsedInput) !HashSet(Point) {
    const grid = input.grid;
    const width = grid[0].len;
    const height = grid.len;

    var current = input.start;
    var visited = HashSet(Point).init();
    var dir = Direction.Up;

    while (true) {
        _ = try visited.add(current);
        const next = current.next(dir) orelse break;

        if(next.x >= width or next.y >= height) {
            break;
        }

        if(grid[next.y][next.x] == '#') {
            dir = dir.next();
        } else {
            current = next;
        }
    }

    return visited;
}

fn isLoop(grid: [][]u8, start: Point, point: Point) !bool {
    const width = grid[0].len;
    const height = grid.len;

    var current = start;
    var visited = HashSet(State).init();
    defer visited.deinit();
    var dir = Direction.Up;

    while (true) {
        const state = State.of(current, dir);

        if(!try visited.add(state)) {
            return true;
        }

        const next = current.next(dir) orelse return false;

        if(next.x >= width or next.y >= height) {
            return false;
        }

        if(grid[next.y][next.x] == '#' or (next.x == point.x and next.y == point.y)) {
            dir = dir.next();
        } else {
            current = next;
        }
    }
}

inline fn parseInput(input: string) !ParsedInput {
    const grid = try splitIntoArray(u8, input, "\n");
    const width = grid[0].len;
    const height = grid.len;

    // look for ^ to initialize start
    var start: Point = undefined;
    for(0..height) |y| {
        for(0..width) |x| {
            if(grid[y][x] == '^') {
                start = Point.of(x, y);
                break;
            }
        }
    }

    return .{ .grid = grid, .start = start };
}

const ParsedInput = struct {
    grid: [][]u8,
    start: Point,
};

const Point = struct {
    x: usize,
    y: usize,

    inline fn of(x: usize, y: usize) Point {
        return .{ .x = x, .y = y };
    }

    inline fn next(self: Point, direction: Direction) ?Point {
        if(direction == Direction.Up and self.y == 0) {
            return null;
        }
        if(direction == Direction.Left and self.x == 0) {
            return null;
        }
        return switch(direction) {
            Direction.Up => of(self.x, self.y - 1),
            Direction.Down => of(self.x, self.y + 1),
            Direction.Left => of(self.x - 1, self.y),
            Direction.Right => of(self.x + 1, self.y),
        };
    }
};

const Direction = enum {
    Up, Down, Left, Right,

    inline fn next(self: Direction) Direction {
        switch(self) {
            Direction.Up => return Direction.Right,
            Direction.Right => return Direction.Down,
            Direction.Down => return Direction.Left,
            Direction.Left => return Direction.Up,
        }
    }
};

const State = struct {
    x: usize, y: usize,
    direction: Direction,

    fn of(position: Point, direction: Direction) State {
        return .{ .x = position.x, .y = position.y, .direction = direction };
    }
};

fn HashSet(comptime T: type) type {
    return struct {
        table: std.AutoHashMap(T, bool),

        pub fn init() HashSet(T) {
            return .{ .table = std.AutoHashMap(T, bool).init(allocator) };
        }

        pub fn add(self: *HashSet(T), e: T) !bool {
            return try self.table.fetchPut(e, true) == null;
        }

        pub fn contains(self: *HashSet(T), e: T) bool {
            return self.table.contains(e);
        }

        pub fn deinit(self: *HashSet(T)) void {
            self.table.deinit();
        }
    };
}

fn splitIntoArray(comptime T: type, input: []const T, separator: []const T) ![][]T {
    const count = std.mem.count(T, input, separator) + 1;
    const split = try allocator.alloc([]T, count);

    var itr = std.mem.split(T, input, separator);
    var i: usize = 0;
    while(itr.next()) |token| {
        //token is const, so we need to copy it
        const copy: []T = try allocator.alloc(T, token.len);
        @memcpy(copy, token);
        split[i] = copy;
        i += 1;
    }

    return split;
}

pub fn main() !void {
    const path = try std.fmt.allocPrint(allocator, "inputs/2024/{}.txt", .{day});
    const file = try std.fs.cwd().openFile(path, .{ .mode = .read_only });
    defer file.close();
    const input = try file.readToEndAlloc(allocator, (1 << 31) - 1);

    var start = std.time.nanoTimestamp();
    const part1Result = try part1(input);
    var end = std.time.nanoTimestamp();
    try stdout.print("--- Part 1: {d:.2}ms ---\n", .{(@as(f128, @floatFromInt(end - start)) / 1_000_000.0)});
    try stdout.print("{any}\n", .{part1Result});

    start = std.time.nanoTimestamp();
    const part2Result = try part2(input);
    end = std.time.nanoTimestamp();
    try stdout.print("--- Part 2: {d:.2}ms ---\n", .{(@as(f128, @floatFromInt(end - start)) / 1_000_000.0)});
    try stdout.print("{any}\n", .{part2Result});
    try stdout.print("----------------------\n", .{});
}
