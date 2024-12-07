const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;
const string = []const u8;

const day: u16 = 7;

pub fn part1(input: string) !usize {
    return try run(input, false);
}

pub fn part2(input: string) !usize {
    return try run(input, true);
}

fn run(input: string, p2: bool) !usize {
    const lines = try splitIntoArray(u8, input, "\n");
    var equations = try allocator.alloc(Equation, lines.len);
    for(0..lines.len) |i| {
        const line = lines[i];
        const splitIndex = std.mem.indexOf(u8, line, ": ") orelse return error.@"Invalid input";
        const result = try parseInt(line[0..splitIndex]);

        const numsStr = try splitIntoArray(u8, line[splitIndex + 2..], " ");
        const nums = try allocator.alloc(usize, numsStr.len);
        for(0..numsStr.len) |j| {
            nums[j] = try parseInt(numsStr[j]);
        }

        equations[i] = .{ .target = result, .nums = nums };
    }

    var sum: usize = 0;
    for(equations) |equation| {
        if(try isValid(equation, 1, equation.nums[0], p2)) {
            sum += equation.target;
        }
    }

    return sum;
}

fn isValid(equation: Equation, index: usize, currentSum: usize, p2: bool) !bool {
    if(index == equation.nums.len) {
        return currentSum == equation.target;
    }

    const sum = currentSum + equation.nums[index];
    if(sum <= equation.target and try isValid(equation, index + 1, sum, p2)) {
        return true;
    }

    const product = currentSum * equation.nums[index];
    if(product <= equation.target and try isValid(equation, index + 1, product, p2)) {
        return true;
    }

    if(p2) {
        const combined = concat(currentSum, equation.nums[index]);
        if(combined <= equation.target and try isValid(equation, index + 1, combined, p2)) {
            return true;
        }
    }

    return false;
}

inline fn parseInt(input: string) !usize {
    return try std.fmt.parseInt(usize, input, 10);
}

inline fn concat(first: usize, second: usize) usize {
    var a = first;
    var b = second;
    while(b != 0) {
        a *= 10;
        b /= 10;
    }

    return a + second;
}

const Equation = struct {
    target: usize,
    nums: []usize,
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

    var timer = try std.time.Timer.start();
    const part1Result = try part1(input);
    var time = timer.lap();
    try stdout.print("--- Part 1: {d:.2}ms ---\n", .{(@as(f128, @floatFromInt(time)) / 1_000_000.0)});
    try stdout.print("{any}\n", .{part1Result});

    const part2Result = try part2(input);
    time = timer.lap();
    try stdout.print("--- Part 2: {d:.2}ms ---\n", .{(@as(f128, @floatFromInt(time)) / 1_000_000.0)});
    try stdout.print("{any}\n", .{part2Result});
    try stdout.print("----------------------\n", .{});
}
