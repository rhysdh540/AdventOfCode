/**
 * <a href="https://adventofcode.com/2023/day/3">Day 3</a>
 */
public class Day3 implements Day.IntDay {
    @Override
    public int run1Int() throws Exception {
        String input = Main.getInput(3).get(0);
        int x = 500, y = 500;
        int[][] grid = new int[1000][1000];
        grid[x][y] = 1;
        for (int i = 0; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case '^' -> y++;
                case 'v' -> y--;
                case '>' -> x++;
                case '<' -> x--;
            }
            grid[x][y]++;
        }
        int count = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell > 0) {
                    count++;
                }
            }
        }
        return count;
    }
    
    @Override
    public int run2Int() throws Exception {
        String input = Main.getInput(3).get(0);
        int x1 = 500, y1 = 500, x2 = 500, y2 = 500;
        int[][] grid = new int[1000][1000];
        grid[x1][y1] = 2;
        for(int i = 0; i < input.length() - 1; i += 2) {
            switch (input.charAt(i)) {
                case '^' -> y1++;
                case 'v' -> y1--;
                case '>' -> x1++;
                case '<' -> x1--;
            }
            switch (input.charAt(i + 1)) {
                case '^' -> y2++;
                case 'v' -> y2--;
                case '>' -> x2++;
                case '<' -> x2--;
            }
            grid[x1][y1]++;
            grid[x2][y2]++;
        }
        int count = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell > 0) {
                    count++;
                }
            }
        }
        return count;
    }
}
                