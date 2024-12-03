const std = @import("std");

const ArrayList = std.ArrayList;
const stdout = std.io.getStdOut().writer();
const allocator = std.heap.c_allocator;
const string = []const u8;

const day: u16 = 3;

pub fn part1(input: []const u8) !usize {
    return run(input, false);
}

pub fn part2(input: string) !usize {
    return run(input, true);
}

fn run(input: string, doEnabled: bool) usize {
    var sum: usize = 0;

    var enabled: bool = true;
    var i: usize = 0;
    o: while(i < input.len) {
        if(doEnabled and i + 3 < input.len and input[i] == 'd' and input[i + 1] == 'o') {
            // either do() or don't()
            if(i + 4 < input.len and input[i + 2] == '(' and input[i + 3] == ')') {
                // do()
                enabled = true;
                i += 4;
            } else if(i + 6 < input.len and input[i + 2] == 'n' and input[i + 3] == '\'' and input[i + 4] == 't' and input[i + 5] == '(' and input[i + 6] == ')') {
                // don't()
                enabled = false;
                i += 7;
            } else {
                i += 1;
            }
        }

        if(enabled and i + 3 < input.len and input[i] == 'm' and input[i + 1] == 'u' and input[i + 2] == 'l' and input[i + 3] == '(') {
            i += 4; // "mul("

            var num1: usize = 0;
            while(i < input.len and input[i] != ',') {
                if(input[i] < '0' or input[i] > '9') {
                    i += 1;
                    continue :o;
                }
                num1 = num1 * 10 + (input[i] - '0');
                i += 1;
            }

            if(i >= input.len or input[i] != ',') continue :o;
            i += 1; // ','

            var num2: usize = 0;
            while(i < input.len and input[i] != ')') {
                if(input[i] < '0' or input[i] > '9') {
                    i += 1;
                    continue :o;
                }
                num2 = num2 * 10 + (input[i] - '0');
                i += 1;
            }

            if(i >= input.len or input[i] != ')') continue :o;
            i += 1; // ')'

            sum += num1 * num2;
        } else {
            i += 1;
        }
    }

    return sum;
}

inline fn parseInt(input: string) !usize {
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
    try stdout.print("----------------------\n", .{});
}
