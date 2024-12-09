const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;
const string = []const u8;

const day: u16 = 2;

pub fn part1(input: string) !usize {
    var lines = std.mem.splitSequence(u8, input, "\n");

    var safeReports: usize = 0;
    while (lines.next()) |report| {
        const split = try splitIntoArray(u8, report, " ");
        if(try isValid(split)) {
            safeReports += 1;
        }
    }

    return safeReports;
}

pub fn part2(input: string) !usize {
    var lines = std.mem.splitSequence(u8, input, "\n");

    var safeReports: usize = 0;
    while (lines.next()) |report| {
        const split = try splitIntoArray(u8, report, " ");

        var valid = false;
        for(0..split.len) |index| {
            const copy = try allocator.alloc(string, split.len - 1);
            @memcpy(copy[0..index], split[0..index]);
            @memcpy(copy[index..], split[index+1..]);

            if(try isValid(copy)) {
                valid = true;
                break;
            }
        }

        if(valid or try isValid(split)) {
            safeReports += 1;
            continue;
        }
    }

    return safeReports;
}

fn isValid(sequence: []const string) !bool {
    var descending: ?bool = null;
    var prevMaybe: ?usize = null;
    for(sequence) |num| {
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

inline fn parseInt(input: string) !usize {
    return try std.fmt.parseInt(usize, input, 10);
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
