const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;

pub fn main() !void {
    const input = try getInput(1);
    try stdout.print("Part 1: {d}\nPart 2: {d}\n", .{try part1(input), try part2(input)});
}

pub fn part1(input: []const u8) !usize {

}

pub fn part2(input: []const u8) !usize {
    _ = input;
    return 0;
}

inline fn parseInt(input: []const u8) !usize {
    return try std.fmt.parseInt(usize, input, 10);
}

fn getInput(day: u16) ![]const u8 {
    const path = try std.fmt.allocPrint(allocator, "inputs/2024/{}.txt", .{day});
    const file = try std.fs.cwd().openFile(path, .{ .mode = .read_only });
    defer file.close();
    return try file.readToEndAlloc(allocator, (2 << 30) - 1);
}