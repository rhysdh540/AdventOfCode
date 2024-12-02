const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;

const day: u16 = 1;

pub fn part1(input: []const u8) !usize {

}

pub fn part2(input: []const u8) !usize {
    _ = input;
    return 0;
}

inline fn parseInt(input: []const u8) !usize {
    return try std.fmt.parseInt(usize, input, 10);
}

pub fn main() !void {
    const path = try std.fmt.allocPrint(allocator, "inputs/2024/{}.txt", .{day});
    const file = try std.fs.cwd().openFile(path, .{ .mode = .read_only });
    defer file.close();
    const input = try file.readToEndAlloc(allocator, (1 << 31) - 1);

    var start = std.time.milliTimestamp();
    const part1Result = try part1(input);
    const end = std.time.milliTimestamp();
    try stdout.print("--- Part 1: {d}ms ---\n", .{end - start});
    try stdout.print("{any}\n", .{part1Result});

    start = std.time.milliTimestamp();
    const part2Result = try part2(input);
    const end2 = std.time.milliTimestamp();
    try stdout.print("--- Part 2: {d}ms ---\n", .{end2 - start});
    try stdout.print("{any}\n", .{part2Result});
    try stdout.print("-------------------\n", .{});
}
