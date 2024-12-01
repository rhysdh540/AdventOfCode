const std = @import("std");
const _input = @import("input.zig");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();

pub fn main() !void {
    const input = try _input.get(1);
    const a1 = try part1(input);
    const a2 = try part2(input);
    try stdout.print("Part 1: {d}\nPart 2: {d}\n", .{a1, a2});
}

pub fn part1(input: []const u8) !usize {
    var lines = std.mem.splitSequence(u8, input, "\n");
    var firstCol = ArrayList(usize).init(std.heap.page_allocator);
    var secondCol = ArrayList(usize).init(std.heap.page_allocator);
    defer firstCol.deinit();
    defer secondCol.deinit();

    var numLines: usize = 0;
    while (lines.next()) |line| {
        var split = std.mem.splitSequence(u8, line, "   ");
        try firstCol.append(try std.fmt.parseInt(usize, split.next().?, 10));
        try secondCol.append(try std.fmt.parseInt(usize, split.next().?, 10));
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
    var firstCol = ArrayList(usize).init(std.heap.page_allocator);
    var secondCol = ArrayList(usize).init(std.heap.page_allocator);
    defer firstCol.deinit();
    defer secondCol.deinit();

    var numLines: usize = 0;
    while (lines.next()) |line| {
        var split = std.mem.splitSequence(u8, line, "   ");
        try firstCol.append(try std.fmt.parseInt(usize, split.next().?, 10));
        try secondCol.append(try std.fmt.parseInt(usize, split.next().?, 10));
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