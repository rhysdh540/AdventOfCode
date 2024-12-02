const std = @import("std");

pub fn getInput(day: u16) ![]const u8 {
    const path = try std.fmt.allocPrint(std.heap.page_allocator, "inputs/2024/{}.txt", .{day});
    const file = try std.fs.cwd().openFile(path, .{ .mode = .read_only });
    defer file.close();
    return try file.readToEndAlloc(std.heap.page_allocator, (2 << 30) - 1);
}

pub inline fn parseInt(input: []const u8) !usize {
    return try std.fmt.parseInt(usize, input, 10);
}

pub fn splitIntoList(comptime T: type, input: []const T, separator: []const T) !std.ArrayList([]const T) {
    var list = std.ArrayList([]const T).init(allocator);
    var split = std.mem.splitSequence(T, input, separator);
    while (split.next()) |token| {
        try list.append(token);
    }
    return list;
}

pub const allocator = std.heap.c_allocator;
