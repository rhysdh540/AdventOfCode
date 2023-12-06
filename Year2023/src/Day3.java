import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;

/**
 * <a href="https://adventofcode.com/2023/day/3">Day 3</a>
 */
public class Day3 implements Day.IntDay {

	@Override
	public int run1Int() throws Exception {
		char[][] grid = getGrid();
		return getSymbols(grid).stream()
				.flatMap(symbol -> getNumbers(grid, symbol).stream())
				.reduce(0, Integer::sum);
	}

	@Override
	public int run2Int() throws Exception {
		char[][] grid = getGrid();
		return getSymbols(grid)
				.stream()
				.filter(Symbol::isGear)
				.map(symbol -> getNumbers(grid, symbol))
				.filter(numbers -> numbers.size() == 2)
				.map(numbers -> numbers.stream().reduce(1, (a, b) -> a * b))
				.reduce(0, Integer::sum);
	}

	private char[][] getGrid() throws Exception {
		List<String> input = Main.getInput(3);
		char[][] grid = new char[input.size()][input.get(0).length()];
		for (int i = 0; i < input.size(); i++) {
			grid[i] = input.get(i).toCharArray();
		}
		return grid;
	}

	private List<Symbol> getSymbols(char[][] grid) {
		List<Symbol> symbols = new ArrayList<>();
		for (int y = 0; y < grid.length; y++) {
			char[] row = grid[y];
			for (int x = 0; x < row.length; x++) {
				char c = row[x];
				if (isSymbol(c)) {
					symbols.add(new Symbol(c, x, y));
				}
			}
		}
		return symbols;
	}

	private Set<Integer> getNumbers(char[][] grid, Symbol symbol) {
		Set<Integer> numbers = new HashSet<>();
		for (int[] surrounding : symbol.getSurrounding()) {
			int x = surrounding[0];
			int y = surrounding[1];

			char c = grid[y][x];
			if (isDigit(c)) {
				// go all the way back to the first digit of the number - 1
				while(x >= 0 && grid[y][x] != '.' && !isSymbol(grid[y][x])) {
					x--;
				}

				// start moving right until we hit a non-digit
				int start = x + 1;
				do x++;
				while(x < grid[y].length && grid[y][x] != '.' && !isSymbol(grid[y][x]));

				String number = new String(Arrays.copyOfRange(grid[y], start, x));
				numbers.add(Integer.parseInt(number));
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
			return new int[][]{
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
