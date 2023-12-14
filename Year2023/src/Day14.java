import aoc.Day.IntDay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.Utils.rotateClockwise;

/**
* <a href="https://adventofcode.com/2023/day/14">Day 14</a>
*/
public class Day14 extends IntDay {

	@Override
	public int run1Int(List<String> input) {
		return countWeight(moveRocksNorth(parseInput(input)));
	}

	final int billion = 1_000_000_000;

	@Override
	public int run2Int(List<String> input) {
		char[][] grid = parseInput(input);

		Map<String, Integer> cache = new HashMap<>();
		int cycle = 0;
		while(cycle < billion) {
			for(int r = 0; r < 4; r++) {
				grid = rotateClockwise(moveRocksNorth(grid));
			}

			String key = arrayToString(grid);
			if(cache.containsKey(key)) {
				int cycleLength = cycle - cache.get(key);
				int remainingCycles = (billion - cycle) % cycleLength;
				cycle = billion - remainingCycles;
			}
			cache.put(key, cycle++);
		}

		return countWeight(grid);
	}

	private char[][] parseInput(List<String> input) {
		char[][] grid = new char[input.size()][input.get(0).length()];
		for(int i = 0; i < input.size(); i++) {
			grid[i] = input.get(i).toCharArray();
		}
		return grid;
	}

	private String arrayToString(char[][] grid) {
		int n = grid.length;
		StringBuilder sb = new StringBuilder(n * n);
		for(char[] row : grid) {
			for(char c : row) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private int countWeight(char[][] grid) {
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

	public char[][] moveRocksNorth(char[][] grid) {
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
