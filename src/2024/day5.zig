const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;
const string = []const u8;

const day: u16 = 5;

pub fn part1(input: string) !usize {
    var rules = ArrayList(Rule).init(allocator);
    defer rules.deinit();
    var sequences = ArrayList([]u8).init(allocator);
    defer sequences.deinit();

    var inputItr = std.mem.splitSequence(u8, input, "\n");
    while(inputItr.next()) |line| {
        if(line.len == 0) break;

        const num1 = 10 * (line[0] - '0') + (line[1] - '0');
        const num2 = 10 * (line[3] - '0') + (line[4] - '0');

        try rules.append(Rule.new(num1, num2));
    }

    while(inputItr.next()) |line| {
        if(line.len == 0) break;

        const count = std.mem.count(u8, line, ",") + 1;
        const sequence = try allocator.alloc(u8, count);
        var itr = std.mem.splitSequence(u8, line, ",");

        var i: usize = 0;
        while(itr.next()) |num| {
            sequence[i] = @intCast(try parseInt(num));
            i += 1;
        }

        try sequences.append(sequence);
    }

    const invalid = try allocator.alloc(bool, sequences.items.len);
    defer allocator.free(invalid);

    o: for(sequences.items, 0..) |sequence, i| {
        for(rules.items) |rule| {
            if(!rule.matches(sequence)) {
                invalid[i] = true;
                continue :o;
            }
        }
    }

    var sum: usize = 0;
    for(0..invalid.len) |i| {
        if(!invalid[i]) {
            const seq = sequences.items[i];
            sum += seq[seq.len / 2];
        }
    }

    return sum;
}

pub fn part2(input: string) !usize {
    var rules = ArrayList(Rule).init(allocator);
    defer rules.deinit();
    var sequences = ArrayList([]u8).init(allocator);
    defer sequences.deinit();

    var inputItr = std.mem.splitSequence(u8, input, "\n");
    while(inputItr.next()) |line| {
        if(line.len == 0) break;

        const num1 = 10 * (line[0] - '0') + (line[1] - '0');
        const num2 = 10 * (line[3] - '0') + (line[4] - '0');

        try rules.append(Rule.new(num1, num2));
    }

    while(inputItr.next()) |line| {
        if(line.len == 0) break;

        const count = std.mem.count(u8, line, ",") + 1;
        const sequence = try allocator.alloc(u8, count);
        var itr = std.mem.splitSequence(u8, line, ",");

        var i: usize = 0;
        while(itr.next()) |num| {
            sequence[i] = @intCast(try parseInt(num));
            i += 1;
        }

        try sequences.append(sequence);
    }

    // actually correct the sequences this time

    var invalid = try allocator.alloc(bool, sequences.items.len);
    for(sequences.items, 0..) |sequence, i| {
        var j: usize = 0;
        while(j < rules.items.len) {
            const rule = rules.items[j];
            if(!rule.matches(sequence)) {
                invalid[i] = true;
                rule.fix(sequence);
                j = 0;
            } else {
                j += 1;
            }
        }
    }

    var sum: usize = 0;
    for(0..invalid.len) |i| {
        if(invalid[i]) {
            const seq = sequences.items[i];
            sum += seq[seq.len / 2];
        }
    }

    return sum;
}

fn indexOf(arr: []const u8, elem: u8) ?usize {
    for(arr, 0..) |e, i| {
        if(e == elem) {
            return i;
        }
    }

    return null;
}

inline fn parseInt(input: string) !usize {
    return try std.fmt.parseInt(usize, input, 10);
}

const Rule = struct {
    first: u8,
    second: u8,

    fn new(first: u8, second: u8) Rule {
        return Rule { .first = first, .second = second };
    }

    fn matches(self: Rule, sequence: []u8) bool {
        const index1 = indexOf(sequence, self.first) orelse return true;
        const index2 = indexOf(sequence, self.second) orelse return true;
        return index1 < index2;
    }

    fn fix(self: Rule, sequence: []u8) void {
        const index1 = indexOf(sequence, self.first) orelse return;
        const index2 = indexOf(sequence, self.second) orelse return;

        sequence[index1] = self.second;
        sequence[index2] = self.first;
    }
};

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
