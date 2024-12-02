const std = @import("std");
const allocator = std.heap.c_allocator;
const stdout = std.io.getStdOut().writer();
const stderr = std.io.getStdErr().writer();

pub fn main() !void {
    const args = try std.process.argsAlloc(allocator);
    defer std.process.argsFree(allocator, args);

    if (args.len < 2 or args.len > 4) {
        try stderr.print("Usage: {s} <language> [year] [day]\n", .{args[0]});
        return;
    }

    //region get current year and day
    const currentTime: u64 = @intCast(std.time.milliTimestamp());
    var days: u64 = @divFloor(currentTime, std.time.ms_per_day);

    var year: u16 = 1970;
    while (true) {
        const isLeap = (@mod(year, 4) == 0) and (@mod(year, 100) != 0 or @mod(year, 400) == 0);
        const daysThisYear: u9 = if (isLeap) 366 else 365;

        if (days < daysThisYear) break;

        days -= daysThisYear;
        year += 1;
    }

    const dayYear: u9 = @intCast(days + 1);
    var dayMonth: u9 = dayYear - 335;
    if (args.len == 2 and dayMonth < 1) {
        try stderr.print("Today is not in December, please specify a day!\n", .{});
        return;
    }
    //endregion

    // Parse arguments
    if (args.len >= 3) {
        const idx: u3 = if (args.len == 4) 3 else 2;

        dayMonth = try std.fmt.parseInt(u9, args[idx], 10);
        const isLeap = (year % 4 == 0) and (year % 100 != 0 or year % 400 == 0);
        const daysThisYear: u9 = if (isLeap) 366 else 365;
        if (dayMonth < 1 or dayMonth > daysThisYear) {
            try stderr.print("Invalid day: {d}\n", .{dayMonth});
            return;
        }
    }

    if (args.len == 4) {
        year = try std.fmt.parseInt(u16, args[2], 10);
        if (year < 1970) {
            try stderr.print("Invalid year: {d}\n", .{year});
            return;
        }
    }
    //endregion

    const file = std.fs.cwd().openFile("languages.json", .{}) catch |err| {
        try stderr.print("Failed to open languages.json: {any}\n", .{err});
        return;
    };
    defer file.close();

    const contents = try file.readToEndAlloc(allocator, 1 << 31);
    const json = try std.json.parseFromSlice([]const Language, allocator, contents, .{});
    const languages = json.value;

    if(languages.len == 0) {
        try stderr.print("No languages found in languages.json\n", .{});
        return;
    }

    for(languages) |lang| {
        if(std.mem.eql(u8, lang.name, "all")) {
            try stderr.print("Language name 'all' is reserved\n", .{});
            return;
        }
    }

    if(std.mem.eql(u8, args[1], "all")) {
        for(languages) |lang| {
            try stdout.print("{s}\n", .{lang.name});
            const command = try lang.getCommand(year, @intCast(dayMonth));
            try runCommand(command);
        }
        return;
    }

    for(languages) |lang| {
        if(std.mem.eql(u8, lang.name, args[1])) {
            const command = try lang.getCommand(year, @intCast(dayMonth));
            try runCommand(command);
            return;
        }
    }

    try stderr.print("Language not found: {s}\n", .{args[1]});
}

const Language = struct {
    name: []const u8,
    template: []const u8,
    run: []const u8,

    fn getCommand(self: Language, year: u16, day: u9) ![]const []const u8 {
        const yearStr = try stringify(year);
        const dayStr = try stringify(day);
        var command = try replace(self.run, "{{year}}", yearStr);
        command = try replace(command, "{{day}}", dayStr);

        var list = std.ArrayList([]const u8).init(allocator);
        var itr = std.mem.split(u8, command, " ");
        while (itr.next()) |part| {
            try list.append(part);
        }

        return list.items;
    }
};

fn stringify(it: anytype) ![]const u8 {
    return try std.fmt.allocPrint(allocator, "{any}", .{it});
}

fn replace(input: []const u8, target: []const u8, replacement: []const u8) ![]const u8 {
    const size = std.mem.replacementSize(u8, input, target, replacement);
    const result = try allocator.alloc(u8, size);
    _ = std.mem.replace(u8, input, target, replacement, result);
    return result;
}

fn runCommand(args: []const []const u8) !void {
    const process = try std.process.Child.run(.{
        .allocator = allocator,
        .argv = args,
    });

    _ = try stdout.write(process.stdout);
    _ = try stderr.write(process.stderr);
}
