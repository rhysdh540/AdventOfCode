const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;
const string = []const u8;

const day: u16 = 4;

pub fn part1(input: string) !usize {
    const grid = try splitIntoArray(u8, input, "\n");
    const rows = grid.len;
    const cols = grid[0].len;

    const directions = [_][2]i8 {
        [_]i8{0, 1},
        [_]i8{1, 0},
        [_]i8{1, 1},
        [_]i8{1, -1},
        [_]i8{0, -1},
        [_]i8{-1, 0},
        [_]i8{-1, -1},
        [_]i8{-1, 1},
    };

    var count: usize = 0;

    for(0..rows) |x| {
        for(0..cols) |y| {
            for(directions) |dir| {
                var found = true;
                for(0..4) |i| {
                    const nx = @as(isize, @intCast(x)) + @as(isize, @intCast(i)) * dir[0];
                    const ny = @as(isize, @intCast(y)) + @as(isize, @intCast(i)) * dir[1];

                    if(nx < 0 or nx >= rows or ny < 0 or ny >= cols
                        or grid[@intCast(nx)][@intCast(ny)] != "XMAS"[i]) {
                        found = false;
                        break;
                    }
                }

                if(found) {
                    count += 1;
                }
            }
        }
    }

    return count;
}

pub fn part2(input: string) !usize {
    const grid = try splitIntoArray(u8, input, "\n");
    const rows = grid.len;
    const cols = grid[0].len;

    var count: usize = 0;
    for(1..rows - 1) |x| {
        for(1..cols - 1) |y| {
            if(grid[x][y] == 'A' and matches(grid, x, y)) {
                count += 1;
            }
        }
    }

    return count;
}

fn matches(grid: []const string, x: usize, y: usize) bool {
    const ul = grid[x - 1][y - 1];
    const ur = grid[x - 1][y + 1];
    const dl = grid[x + 1][y - 1];
    const dr = grid[x + 1][y + 1];

    //M.M
    //.A.
    //S.S
    if(ul == 'M' and ur == 'M' and dl == 'S' and dr == 'S') {
        return true;
    }

    //S.S
    //.A.
    //M.M
    if(ul == 'S' and ur == 'S' and dl == 'M' and dr == 'M') {
        return true;
    }

    //M.S
    //.A.
    //M.S
    if(ul == 'M' and ur == 'S' and dl == 'M' and dr == 'S') {
        return true;
    }

    //S.M
    //.A.
    //S.M
    if(ul == 'S' and ur == 'M' and dl == 'S' and dr == 'M') {
        return true;
    }

    return false;
}

fn splitIntoArray(comptime T: type, input: []const T, separator: []const T) ![]const []const T {
    const count = std.mem.count(T, input, separator) + 1;
    const split = try allocator.alloc([]const T, count);

    var itr = std.mem.split(T, input, separator);
    var i: usize = 0;
    while(itr.next()) |token| {
        split[i] = token;
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
