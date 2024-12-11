const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;
const string = []const u8;

const day: u16 = 11;

pub fn part1(input: string) !usize {
    return try run(input, 25);
}

pub fn part2(input: string) !usize {
    return try run(input, 75);
}

fn run(input: string, times: usize) !usize {
    const numStones = std.mem.count(u8, input, " ");
    var stones = try ArrayList(usize).initCapacity(allocator, numStones);
    defer stones.deinit();

    var itr = std.mem.splitSequence(u8, input, " ");
    while (itr.next()) |token| {
        const stone = try parseInt(token);
        try stones.append(stone);
    }

    var memo = std.AutoHashMap(Key, usize).init(allocator);
    defer memo.deinit();

    var sum: usize = 0;
    for(stones.items) |stone| {
        sum += try count(stone, times, &memo);
    }

    return sum;
}

fn count(stone: usize, times: usize, memo: *std.AutoHashMap(Key, usize)) !usize {
    if(times == 0) return 1;

    const key = Key{ .stone = stone, .times = times };
    if(memo.contains(key)) return memo.get(key).?;

    var nextSum: usize = 0;

    if(stone == 0) {
        nextSum += try count(1, times - 1, memo);
    } else {
        const digits = std.math.log10(stone) + 1;
        if(digits & 1 == 0) {
            const half = digits / 2;
            const divisor = std.math.pow(usize, 10, half);
            nextSum += try count(stone / divisor, times - 1, memo)
                + try count(stone % divisor, times - 1, memo);
        } else {
            nextSum += try count(stone * 2024, times - 1, memo);
        }
    }

    try memo.put(key, nextSum);
    return nextSum;
}

const Key = struct {
    stone: usize,
    times: usize,
};

inline fn parseInt(input: string) !usize {
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
