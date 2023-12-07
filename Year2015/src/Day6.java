import java.util.List;
import java.util.function.BiFunction;

/**
 * <a href="https://adventofcode.com/2015/day/6">Day 6</a>
 */
public class Day6 extends Day.IntDay {

    @Override
    public int run1Int(List<String> input) {
        return runCommon((command, currentValue) -> {
            if (command == null) {
                return 1 - currentValue;
            } else {
                return command.equals("on") ? 1 : 0;
            }
        });
    }

    @Override
    public int run2Int(List<String> input) {
        return runCommon((command, currentValue) -> {
            if (command == null) {
                return currentValue + 2;
            } else {
                return Math.max(0, currentValue + (command.equals("on") ? 1 : -1));
            }
        });
    }

    private int runCommon(BiFunction<String, Integer, Integer> handler) {
        List<String> input = getInput();
        int[][] grid = new int[1000][1000];
        for (String line : input) {
            String[] parts = line.split(" ");
            String[] to = parts[parts.length - 1].split(","),
                    from = parts[parts.length - 3].split(",");
            int fromX = Utils.fastParseInt(from[0]), fromY = Utils.fastParseInt(from[1]);
            int toX = Utils.fastParseInt(to[0]), toY = Utils.fastParseInt(to[1]);

            switch (parts[0]) {
                case "turn" -> {
                    for (int x = fromX; x <= toX; x++) {
                        for (int y = fromY; y <= toY; y++) {
                            grid[x][y] = handler.apply(parts[1], grid[x][y]);
                        }
                    }
                }
                case "toggle" -> {
                    for (int x = fromX; x <= toX; x++) {
                        for (int y = fromY; y <= toY; y++) {
                            grid[x][y] = handler.apply(null, grid[x][y]);
                        }
                    }
                }
            }
        }
        int count = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                count += cell;
            }
        }
        return count;
    }
}
