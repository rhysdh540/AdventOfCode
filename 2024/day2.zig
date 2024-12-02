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
        const split = try splitIntoList(u8, report, " ");
        defer split.deinit();
        if(try isValid(split.items)) {
            safeReports += 1;
        }
    }

    return safeReports;
}

pub fn part2(input: string) !usize {
    var lines = std.mem.splitSequence(u8, input, "\n");

    var safeReports: usize = 0;
    while (lines.next()) |report| {
        const split = try splitIntoList(u8, report, " ");
        defer split.deinit();

        var valid = false;
        for(0..split.items.len) |index| {
            var copy = try ArrayList(string).initCapacity(allocator, split.items.len - 1);
            defer copy.deinit();
            try copy.appendSlice(split.items[0..index]);
            try copy.appendSlice(split.items[index+1..]);

            if(try isValid(copy.items)) {
                valid = true;
                break;
            }
        }

        if(valid or try isValid(split.items)) {
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

fn splitIntoList(comptime T: type, input: []const T, separator: []const T) !ArrayList([]const T) {
    var list = ArrayList([]const T).init(allocator);
    var split = std.mem.splitSequence(T, input, separator);
    while (split.next()) |token| {
        try list.append(token);
    }
    return list;
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
    try stdout.print("-------------------\n", .{});
}
