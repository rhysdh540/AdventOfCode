import aoc.Day.IntDay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <a href="https://adventofcode.com/2023/day/14">Day 14</a>
*/
public class Day14 extends IntDay {
	@Override
	public int run1Int(List<String> input) {
		return countRocks(moveRocks(parseInput(input)));
	}

	@Override
	public int run2Int(List<String> input) {
		char[][] grid = parseInput(input);

		Map<String, Integer> cache = new HashMap<>();
		int cycle = 0;
		while(cycle < 1_000_000_000) {
			for(int r = 0; r < 4; r++) {
				grid = rotate(moveRocks(grid));
			}

			String key = deepToString(grid);
			if(cache.containsKey(key)) {
				int cycleLength = cycle - cache.get(key);
				int remainingCycles = (1_000_000_000 - cycle) % cycleLength;
				cycle = 1_000_000_000 - remainingCycles;
			}
			cache.put(key, cycle++);
		}

		return countRocks(grid);
	}

	private char[][] parseInput(List<String> input) {
		char[][] grid = new char[input.size()][input.get(0).length()];
		for(int i = 0; i < input.size(); i++) {
			grid[i] = input.get(i).toCharArray();
		}
		return grid;
	}

	private String deepToString(char[][] grid) {
		StringBuilder sb = new StringBuilder();
		for(char[] row : grid) {
			for(char c : row) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private int countRocks(char[][] grid) {
		int count = 0;
		for(int i = 0; i < grid.length; i++) {
			char[] row = grid[i];
			for(char c : row) {
				if(c == 'O') {
					count += grid.length - i;
				}
			}
		}

		return count;
	}

	private char[][] rotate(char[][] grid) {
		int n = grid.length;
		for(int i = 0; i < n / 2; i++) {
			for(int j = i; j < n - i - 1; j++) {
				char temp = grid[i][j];
				grid[i][j] = grid[n - j - 1][i];
				grid[n - j - 1][i] = grid[n - i - 1][n - j - 1];
				grid[n - i - 1][n - j - 1] = grid[j][n - i - 1];
				grid[j][n - i - 1] = temp;
			}
		}
		return grid;
	}

	public char[][] moveRocks(char[][] grid) {
		for(int i = 0; i < grid.length; i++) {
			char[] row = grid[i];
			for(int j = 0; j < row.length; j++) {
				char c = row[j];
				if(c == 'O') {
					int k = i;
					while(k > 0 && grid[k - 1][j] == '.') {
						grid[k--][j] = '.';
						grid[k][j] = 'O';
					}
				}
			}
		}
		return grid;
	}
}
