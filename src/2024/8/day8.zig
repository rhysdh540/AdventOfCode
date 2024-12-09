const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;
const string = []const u8;

const day: u16 = 8;

pub fn part1(input: string) !usize {
    const grid = try splitIntoArray(u8, input, "\n");
    const width = grid[0].len;
    const height = grid.len;

    var nodes = std.AutoHashMap(u8, ArrayList(Point)).init(allocator);
    defer nodes.deinit();
    for(0..height) |y| {
        for(0..width) |x| {
            const c = grid[y][x];
            if(c == '.') {
                continue;
            }

            const point = Point.of(@intCast(y), @intCast(x));
            var points = nodes.get(c) orelse b: {
                const list = ArrayList(Point).init(allocator);
                defer list.deinit();
                break :b list;
            };

            try points.append(point);
            try nodes.put(c, points);
        }
    }

    var antinodes = HashSet(Point).init();
    defer antinodes.deinit();
    var itr = nodes.valueIterator();
    while(itr.next()) |points| {
        for(0..points.items.len) |i| {
            for(i + 1..points.items.len) |j| {
                const p1 = points.items[i];
                const p2 = points.items[j];

                const dx = p2.x - p1.x;
                const dy = p2.y - p1.y;

                const a1 = Point.of(p1.x - dx, p1.y - dy);
                const a2 = Point.of(p2.x + dx, p2.y + dy);

                if(a1.x >= 0 and a1.x < height and a1.y >= 0 and a1.y < width) {
                    _ = try antinodes.add(a1);
                }

                if(a2.x >= 0 and a2.x < height and a2.y >= 0 and a2.y < width) {
                    _ = try antinodes.add(a2);
                }
            }
        }
    }

    return antinodes.table.count();
}

pub fn part2(input: string) !usize {
    const grid = try splitIntoArray(u8, input, "\n");
    const width = grid[0].len;
    const height = grid.len;

    var nodes = std.AutoHashMap(u8, ArrayList(Point)).init(allocator);
    defer nodes.deinit();
    for(0..height) |y| {
        for(0..width) |x| {
            const c = grid[y][x];
            if(c == '.') {
                continue;
            }

            const point = Point.of(@intCast(y), @intCast(x));
            var points = nodes.get(c) orelse b: {
                const list = ArrayList(Point).init(allocator);
                defer list.deinit();
                break :b list;
            };

            try points.append(point);
            try nodes.put(c, points);
        }
    }

    var antinodes = HashSet(Point).init();
    defer antinodes.deinit();
    var itr = nodes.valueIterator();
    while(itr.next()) |points| {
        for(0..points.items.len) |i| {
            for(i + 1..points.items.len) |j| {
                const p1 = points.items[i];
                const p2 = points.items[j];

                const dx = p2.x - p1.x;
                const dy = p2.y - p1.y;

                // theorietically we should divide dx and dy by gcd(dx, dy)
                // but it's not necessary because of how the input is structured

                var x = p1.x + dx;
                var y = p1.y + dy;
                while(x >= 0 and x < height and y >= 0 and y < width) {
                    const a = Point.of(x, y);
                    _ = try antinodes.add(a);
                    x += dx;
                    y += dy;
                }

                x = p1.x - dx;
                y = p1.y - dy;
                while(x >= 0 and x < height and y >= 0 and y < width) {
                    const a = Point.of(x, y);
                    _ = try antinodes.add(a);
                    x -= dx;
                    y -= dy;
                }

                _ = try antinodes.add(p1);
            }
        }
    }

    return antinodes.table.count();
}

inline fn parseInt(input: string) !usize {
    return try std.fmt.parseInt(usize, input, 10);
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

const Point = struct {
    x: i32,
    y: i32,

    inline fn of(x: i32, y: i32) Point {
        return .{ .x = x, .y = y };
    }
};

fn HashSet(comptime T: type) type {
    return struct {
        table: std.AutoHashMap(T, bool),

        pub fn init() @This() {
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
