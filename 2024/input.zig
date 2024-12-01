const std = @import("std");

pub fn get(day: u16) ![]const u8 {
    const path = try std.fmt.allocPrint(std.heap.page_allocator, "inputs/2024/{}.txt", .{day});
    const file = try std.fs.cwd().openFile(path, .{ .mode = .read_only });
    defer file.close();
    return try file.readToEndAlloc(std.heap.page_allocator, (2 << 30) - 1);
}