const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;

const day: u16 = 1;

pub fn part1(input: []const u8) !usize {
    var lines = std.mem.splitSequence(u8, input, "\n");
    var firstCol = ArrayList(usize).init(allocator);
    var secondCol = ArrayList(usize).init(allocator);
    defer firstCol.deinit();
    defer secondCol.deinit();

    var numLines: usize = 0;
    while (lines.next()) |line| {
        var split = std.mem.splitSequence(u8, line, "   ");
        try firstCol.append(try parseInt(split.next().?));
        try secondCol.append(try parseInt(split.next().?));
        numLines += 1;
    }

    std.mem.sort(usize, firstCol.items, {}, std.sort.asc(usize));
    std.mem.sort(usize, secondCol.items, {}, std.sort.asc(usize));

    var i: usize = 0;
    var sum: usize = 0;
    while (i < numLines) {
        const first = firstCol.items[i];
        const second = secondCol.items[i];
        const diff = @abs(@as(i128, first) - @as(i128, second));
        sum += @intCast(diff);
        i += 1;
    }

    return sum;
}

pub fn part2(input: []const u8) !usize {
    var lines = std.mem.splitSequence(u8, input, "\n");
    var firstCol = ArrayList(usize).init(allocator);
    var secondCol = ArrayList(usize).init(allocator);
    defer firstCol.deinit();
    defer secondCol.deinit();

    var numLines: usize = 0;
    while (lines.next()) |line| {
        var split = std.mem.splitSequence(u8, line, "   ");
        try firstCol.append(try parseInt(split.next().?));
        try secondCol.append(try parseInt(split.next().?));
        numLines += 1;
    }

    var sum: usize = 0;
    var i: usize = 0;
    while (i < numLines) {
        const num = firstCol.items[i];

        var j: usize = 0;
        var count: usize = 0;
        while (j < numLines) {
            if (secondCol.items[j] == num) {
                count += 1;
            }
            j += 1;
        }

        sum += count * num;
        i += 1;
    }

    return sum;
}

inline fn parseInt(input: []const u8) !usize {
    return try std.fmt.parseInt(usize, input, 10);
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