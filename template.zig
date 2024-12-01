const lib = @import("lib.zig");
const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();

pub fn main() !void {
    const input = try lib.getInput(1);
    try stdout.print("Part 1: {d}\nPart 2: {d}\n", .{try part1(input), try part2(input)});
}

pub fn part1(input: []const u8) !usize {

}

pub fn part2(input: []const u8) !usize {

}