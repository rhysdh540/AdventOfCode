import java.util.List;

/**
 * <a href="https://adventofcode.com/2023/day/3">Day 3</a>
 */
public class Day3 implements Day.IntDay {
    @Override
    public int run1Int() throws Exception {
		List<String> input = Main.getInput(3);
        input.add(0, ".".repeat(input.get(0).length()));
        input.add(".".repeat(input.get(0).length()));
        char[][] map = input.stream()
                .map(s -> "." + s + ".")
                .map(String::toCharArray)
                .toArray(char[][]::new);

        int sum = 0;

        for(int i = 0; i < map.length; i++) {
            char[] row = map[i];
            for(int j = 0; j < row.length; j++) {
                char c = row[j];
                if(Character.isDigit(c)) {
                    int num = c - '0';
                    int k = j + 1;
                    while(k < row.length && Character.isDigit(row[k])) {
                        num *= 10;
                        num += row[k] - '0';
                        k++;
                    }

                    int numLength = k - j;

                    boolean nextToSymbol = false;
                    outer:
                    for(int l = i - 1; l < i + 2; l++) {
                        for(int m = j - 1; m < j + numLength + 1; m++) {
                            if(isSymbol(map[l][m])) {
                                nextToSymbol = true;
                                break outer;
                            }
                        }
                    }
                    if(nextToSymbol) {
                        sum += num;
                    }
                    j += numLength - 1;
                }
            }
        }

        return sum;
    }
    
    @Override
    public int run2Int() throws Exception {
        List<String> input = Main.getInput(3, "example");
        input.add(0, ".".repeat(input.get(0).length()));
        input.add(".".repeat(input.get(0).length()));
        char[][] map = input.stream()
                .map(s -> "." + s + ".")
                .map(String::toCharArray)
                .toArray(char[][]::new);

        int sum = 0;

        // find all * next to 2 numbers and sum the products of those two numbers
        for(int i = 0; i < map.length; i++) {
            char[] row = map[i];
            // send help
        }
        return sum;
    }

    private int findNum(char[][] map, int i, int j, int dir) {
        int num = 0;
        int k = j + dir;
        while(k < map[i].length && Character.isDigit(map[i][k])) {
            num *= 10;
            num += map[i][k] - '0';
            k += dir;
        }
        return num;
    }

    private boolean isSymbol(char c) {
        return c != '.' && !Character.isDigit(c);
    }
}
                