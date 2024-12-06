import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class Day6 {
	public static Object part1(String input) throws Throwable {
		String[] lines = input.split("\n");
		char[][] grid = new char[lines.length][];

		Point pos = new Point(0, 0);
		for(int i = 0; i < lines.length; i++) {
			char[] line = lines[i].toCharArray();
			for(int j = 0; j < line.length; j++) {
				if(line[j] == '^') {
					pos = new Point(j, i);
				}
			}

			grid[i] = line;
		}

		Set<Point> visited = new HashSet<>();
		int direction = 0; // 0 = up, 1 = right, 2 = down, 3 = left

		while(true) {
			visited.add(pos.getLocation());

			int newX = pos.x;
			int newY = pos.y;

			switch(direction) {
				case 0 -> newY--;
				case 1 -> newX++;
				case 2 -> newY++;
				case 3 -> newX--;
				default -> throw new RuntimeException("Invalid direction");
			}

			// Check if the guard has exited the grid
			if(newY < 0 || newY >= grid.length || newX < 0 || newX >= grid[newY].length) {
				break;
			}

			char next = grid[newY][newX];
			if(next == '#') {
				direction = (direction + 1) % 4;
			} else {
				pos.x = newX;
				pos.y = newY;
			}
		}

		return visited.size();
	}

	public static Object part2(String input) throws Throwable {
		String[] lines = input.split("\n");
		char[][] grid = new char[lines.length][];

		Point pos = new Point(0, 0);
		for(int i = 0; i < lines.length; i++) {
			char[] line = lines[i].toCharArray();
			for(int j = 0; j < line.length; j++) {
				if(line[j] == '^') {
					pos = new Point(j, i);
				}
			}

			grid[i] = line;
		}

		int count = 0;

		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(grid[i][j] == '#') {
					continue;
				}

				grid[i][j] = '#';

				Set<PointDir> visited = new HashSet<>();

				Point current = pos.getLocation();
				int direction = 0;

				while(true) {
					PointDir pd = new PointDir(current.x, current.y, direction);
					if(visited.contains(pd)) {
						count++;
						break;
					}

					visited.add(pd);

					int newX = current.x;
					int newY = current.y;

					switch(direction) {
						case 0 -> newY--;
						case 1 -> newX++;
						case 2 -> newY++;
						case 3 -> newX--;
						default -> throw new RuntimeException("Invalid direction");
					}

					// Check if the guard has exited the grid
					if(newY < 0 || newY >= grid.length || newX < 0 || newX >= grid[newY].length) {
						break;
					}

					char next = grid[newY][newX];
					if(next == '#') {
						direction = (direction + 1) % 4;
					} else {
						current.x = newX;
						current.y = newY;
					}
				}

				grid[i][j] = '.';
			}
		}

		return count;
	}

	private record PointDir(int x, int y, int direction) {
	}

	public static void main(String[] args) throws Throwable {
		String input = Files.readString(Paths.get("inputs/2024/6.txt"));

		long start = System.nanoTime();
		Object result = part1(input);
		long end = System.nanoTime();
		System.out.printf("--- Part 1: %.2fms ---%n", (end - start) / 1e6);
		System.out.println(result);

		start = System.nanoTime();
		result = part2(input);
		end = System.nanoTime();
		System.out.printf("--- Part 2: %.2fms ---%n", (end - start) / 1e6);
		System.out.println(result);
		System.out.println("----------------------");
	}
}
