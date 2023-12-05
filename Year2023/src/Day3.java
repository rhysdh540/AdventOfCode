import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @SuppressWarnings("EqualsBetweenInconvertibleTypes") // yeah yeah shut up
    public int run2Int() throws Exception {
        char[][] grid = getGrid();
        return getSymbols(grid)
                .stream()
                .filter(symbol -> symbol.equals('*'))
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
                // go all the way left until we hit a ., x=0, or a symbol knowing we are past the left end of the number
                while (x >= 0 && grid[y][x] != '.' && !isSymbol(grid[y][x])) {
                    x--;
                }

                // go back to the first digit of the number
                x++;

                StringBuilder number = new StringBuilder();

                // start moving right to read the entire number, stopping at a . or if we hit the right end of the grid
                while (x < grid[y].length && grid[y][x] != '.' && !isSymbol(grid[y][x])) {
                    number.append(grid[y][x]);
                    x++;
                }

                numbers.add(Integer.parseInt(number.toString()));
            }
        }
        return numbers;
    }

    private boolean isSymbol(char c) {
        return String.valueOf(c).matches("[@#$%^&*\\-=+/]");
    }

    record Symbol(char symbol, int x, int y) {
        int[][] getSurrounding() {
            return new int[][]{
                    {x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1},
                    {x - 1, y}, {x + 1, y},
                    {x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}
            };
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Character c)
                return symbol == c;
            if(obj instanceof String s)
                return s.length() == 1 && symbol == s.charAt(0);
            return (obj instanceof Symbol other) && symbol == other.symbol;
        }
    }
}
                