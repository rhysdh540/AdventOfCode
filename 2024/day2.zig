const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;

pub fn main() !void {
    const input = try getInput(2);
    try stdout.print("Part 1: {d}\nPart 2: {d}\n", .{try part1(input), try part2(input)});
}

pub fn part1(input: []const u8) !usize {
    var lines = std.mem.splitSequence(u8, input, "\n");

    var safeReports: usize = 0;
    while (lines.next()) |report| {
        const split = try splitIntoList(u8, report, " ");
        defer split.deinit();
        if(try isValid(split)) {
            safeReports += 1;
        }
    }

    return safeReports;
}

pub fn part2(input: []const u8) !usize {
    var lines = std.mem.splitSequence(u8, input, "\n");

    var safeReports: usize = 0;
    while (lines.next()) |report| {
        const split = try splitIntoList(u8, report, " ");
        defer split.deinit();

        if(try isValid(split)) {
            safeReports += 1;
            continue;
        }

        for(0..split.items.len) |index| {
            var copy = ArrayList([]const u8).init(allocator);
            defer copy.deinit();
            try copy.appendSlice(split.items[0..index]);
            try copy.appendSlice(split.items[index+1..]);

            if(try isValid(copy)) {
                safeReports += 1;
                break;
            }
        }
    }

    return safeReports;
}

fn isValid(sequence: ArrayList([]const u8)) !bool {
    var descending: ?bool = null;
    var prevMaybe: ?usize = null;
    for(sequence.items) |num| {
        const current = try parseInt(num);
        if(prevMaybe == null) {
            prevMaybe = current;
            continue;
        }

        const prev = prevMaybe orelse unreachable;

        if(descending == null) {
            descending = prev > current;
        }

        if(descending != (prev > current)) {
            return false;
        }

        const diff = @abs(@as(i128, prev) - @as(i128, current));
        if (diff < 1 or diff > 3) {
            return false;
        }

        prevMaybe = current;
    }

    return true;
}

inline fn parseInt(input: []const u8) !usize {
    return try std.fmt.parseInt(usize, input, 10);
}

fn splitIntoList(comptime T: type, input: []const T, separator: []const T) !std.ArrayList([]const T) {
    var list = std.ArrayList([]const T).init(allocator);
    var split = std.mem.splitSequence(T, input, separator);
    while (split.next()) |token| {
        try list.append(token);
    }
    return list;
}

fn getInput(day: u16) ![]const u8 {
    const path = try std.fmt.allocPrint(allocator, "inputs/2024/{}.txt", .{day});
    const file = try std.fs.cwd().openFile(path, .{ .mode = .read_only });
    defer file.close();
    return try file.readToEndAlloc(allocator, (2 << 30) - 1);
}
