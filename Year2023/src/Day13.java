import aoc.Day.IntDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* <a href="https://adventofcode.com/2023/day/13">Day 13</a>
*/
public class Day13 extends IntDay {
	@Override
	public int run1Int(List<String> input) {
		List<String[]> parsedInput = parseInput(input);

		int sum = 0;
		for(String[] grid : parsedInput) {
			int found = findReflection(grid, false) * 100;
			if(found == 0) {
				found = findReflection(rotate(grid), false);
			}
			sum += found;
		}
		return sum;
	}

	@Override
	public int run2Int(List<String> input) {
		List<String[]> parsedInput = parseInput(input);
		int sum = 0;
		for(String[] grid : parsedInput) {
			int found = findReflection(grid, true) * 100;
			if(found == 0) {
				found = findReflection(rotate(grid), true);
			}
			sum += found;
		}
		return sum;
	}

	private List<String[]> parseInput(List<String> input) {
		List<String[]> output = new ArrayList<>();
		List<String> lineOutput = new ArrayList<>();
		for(String line : input) {
			if(line.isEmpty()) {
				output.add(lineOutput.toArray(new String[0]));
				lineOutput.clear();
			} else {
				lineOutput.add(line);
			}
		}
		output.add(lineOutput.toArray(new String[0]));
		return output;
	}

	private String[] reverseArray(String[] array) {
		String[] reversedArray = new String[array.length];
		for(int i = 0; i < array.length; i++) {
			reversedArray[i] = array[array.length - i - 1];
		}
		return reversedArray;
	}

	private int findReflection(String[] grid, boolean fixSmudge) {
		for (int r = 1; r < grid.length; r++) {
			String[] above = Arrays.copyOfRange(grid,0, r);
			String[] below = Arrays.copyOfRange(grid, r, grid.length);
			String[] reversedAboveArray = reverseArray(above);

			boolean isSmudgeFixed = fixSmudge && diff(reversedAboveArray, below) == 1;
			boolean isReflectionFound = !fixSmudge && arrayEquals(reversedAboveArray, below);

			if (isSmudgeFixed || isReflectionFound) {
				return r;
			}
		}
		return 0;
	}

	private boolean arrayEquals(String[] a, String[] b) {
		int len = Math.min(a.length, b.length);
		for(int i = 0; i < len; i++) {
			if(!a[i].equals(b[i])) return false;
		}
		return true;
	}

	private int diff(String[] a, String[] b) {
		int len = Math.min(a.length, b.length);
		int diff = 0;
		int width = a[0].length();
		for(int i = 0; i < len; i++) {
			for(int j = 0; j < width; j++) {
				if(a[i].charAt(j) != b[i].charAt(j)) {
					diff++;
				}
				if(diff > 1) return diff;
			}
		}
		return diff;
	}

	private String[] rotate(String[] grid) {
		int width = grid.length;
		int length = grid[0].length();
		String[] rotatedGrid = new String[length];
		for(int i = 0; i < length; i++) {
			StringBuilder sb = new StringBuilder(width);
			for(String s : grid) {
				sb.append(s.charAt(i));
			}
			rotatedGrid[i] = sb.toString();
		}
		return rotatedGrid;
	}
}
