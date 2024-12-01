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

}

pub fn part2(input: []const u8) !usize {

}