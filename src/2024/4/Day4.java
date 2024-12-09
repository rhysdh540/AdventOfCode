import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

public class Day4 {
	public static Object part1(String input) throws Throwable {
		// Split the input into a grid of characters
		String[] lines = input.split("\n");
		int rows = lines.length;
		int cols = lines[0].length();
		char[][] grid = new char[rows][cols];
		for (int i = 0; i < rows; i++) {
			grid[i] = lines[i].toCharArray();
		}

		int count = 0;

		int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}, {0, -1}, {-1, 0}, {-1, -1}, {-1, 1}};

		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				for (int[] dir : directions) {
					boolean found = true;
					for (int i = 0; i < 4; i++) {
						int nx = x + i * dir[0];
						int ny = y + i * dir[1];
						if (nx < 0 || nx >= rows || ny < 0 || ny >= cols || grid[nx][ny] != "XMAS".charAt(i)) {
							found = false;
							break;
						}
					}

					if (found) {
						count++;
					}
				}
			}
		}

		return count;
	}

	public static Object part2(String input) throws Throwable {
		String[] lines = input.split("\n");
		int rows = lines.length;
		int cols = lines[0].length();
		char[][] grid = new char[rows][cols];
		for (int i = 0; i < rows; i++) {
			grid[i] = lines[i].toCharArray();
		}

		int count = 0;
		for(int x = 1; x < rows - 1; x++) {
			for(int y = 1; y < cols - 1; y++) {
				if(grid[x][y] == 'A' && matches(grid, x, y)) {
					count++;
				}
			}
		}

		return count;
	}

	private static boolean matches(char[][] grid, int x, int y) {
		// M.M
		// .A.
		// S.S
		if(grid[x - 1][y - 1] == 'M' && grid[x - 1][y + 1] == 'M' && grid[x + 1][y - 1] == 'S' && grid[x + 1][y + 1] == 'S') {
			return true;
		}

		// S.S
		// .A.
		// M.M
		if(grid[x - 1][y - 1] == 'S' && grid[x - 1][y + 1] == 'S' && grid[x + 1][y - 1] == 'M' && grid[x + 1][y + 1] == 'M') {
			return true;
		}

		// M.S
		// .A.
		// M.S
		if(grid[x - 1][y - 1] == 'M' && grid[x - 1][y + 1] == 'S' && grid[x + 1][y - 1] == 'M' && grid[x + 1][y + 1] == 'S') {
			return true;
		}

		// S.M
		// .A.
		// S.M
		if(grid[x - 1][y - 1] == 'S' && grid[x - 1][y + 1] == 'M' && grid[x + 1][y - 1] == 'S' && grid[x + 1][y + 1] == 'M') {
			return true;
		}

		return false;
	}

	public static void main(String[] args) throws Throwable {
		String input = Files.readString(Paths.get("inputs/2024/4.txt"));
		var o = System.out;
		System.setOut(new PrintStream(new ByteArrayOutputStream()));
		part1(input); part2(input);
		System.setOut(o);

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
