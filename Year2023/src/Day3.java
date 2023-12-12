import aoc.Day.IntDay;
import util.IntHashSet;
import util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2023/day/3">Day 3</a>
 */
public class Day3 extends IntDay {

	@Override
	public int run1Int(List<String> input) {
		char[][] grid = getGrid();
		int total = 0;
		for(Symbol symbol : getSymbols(grid)) {
			for(int i : getNumbers(grid, symbol)) {
				total += i;
			}
		}
		return total;
	}

	@Override
	public int run2Int(List<String> input) {
		char[][] grid = getGrid();
		int total = 0;
		for(Symbol symbol : getSymbols(grid)) {
			if(!symbol.isGear()) continue;
			IntHashSet numbers = getNumbers(grid, symbol);
			if(numbers.size() != 2) continue;
			int result = 1;
			for(int number : numbers) {
				result *= number;
			}
			total += result;
		}
		return total;
	}

	private char[][] getGrid() {
		List<String> input = super.getInput();
		char[][] grid = new char[input.size()][input.get(0).length()];
		for(int i = 0; i < input.size(); i++) {
			grid[i] = input.get(i).toCharArray();
		}
		return grid;
	}

	private List<Symbol> getSymbols(char[][] grid) {
		List<Symbol> symbols = new ArrayList<>();
		for(int y = 0; y < grid.length; y++) {
			char[] row = grid[y];
			for(int x = 0; x < row.length; x++) {
				char c = row[x];
				if(isSymbol(c)) {
					symbols.add(new Symbol(c, x, y));
				}
			}
		}
		return symbols;
	}

	private IntHashSet getNumbers(char[][] grid, Symbol symbol) {
		IntHashSet numbers = new IntHashSet();
		for(int[] surrounding : symbol.getSurrounding()) {
			int x = surrounding[0];
			int y = surrounding[1];

			char c = grid[y][x];
			if(Utils.isDigit(c)) {
				// go all the way back to the first digit of the number - 1
				while(x >= 0 && grid[y][x] != '.' && !isSymbol(grid[y][x])) {
					x--;
				}

				// start moving right until we hit a non-digit
				x++;
				int start = x;
				while(x < grid[y].length && grid[y][x] != '.' && !isSymbol(grid[y][x])) {
					x++;
				}

				String number = new String(Arrays.copyOfRange(grid[y], start, x));
				numbers.add(Utils.fastParseInt(number));
			}
		}
		return numbers;
	}

	private final Pattern symbolPattern = Pattern.compile("[@#$%^&*\\-=+/]");

	private boolean isSymbol(char c) {
		return symbolPattern.matcher(c + "").matches();
	}

	record Symbol(char symbol, int x, int y) {
		int[][] getSurrounding() {
			return new int[][] {
					{x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1},
					{x - 1, y}, {x + 1, y},
					{x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}
			};
		}

		public boolean isGear() {
			return symbol == '*';
		}
	}
}
