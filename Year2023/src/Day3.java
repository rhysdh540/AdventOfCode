import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isDigit;

/**
 * <a href="https://adventofcode.com/2023/day/3">Day 3</a>
 */
public class Day3 implements Day.IntDay {
    @Override
    public int run1Int() throws Exception {
		List<String> input = Main.getInput(3);
        input.add(0, ".".repeat(input.get(0).length()));
        input.add(input.get(0));
        char[][] map = input.stream()
                .map(s -> "." + s + ".")
                .map(String::toCharArray)
                .toArray(char[][]::new);

        int sum = 0;

        for(int i = 1; i < map.length - 1; i++) {
            char[] row = map[i];
            for(int j = 1; j < row.length - 1; j++) {
                char c = row[j];
                if(isDigit(c)) {
                    int num = c - '0';
                    int k = j + 1;
                    while(k < row.length && isDigit(row[k])) {
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
        input.add(input.get(0));
        char[][] map = input.stream()
                .map(s -> "." + s + ".")
                .map(String::toCharArray)
                .toArray(char[][]::new);

        int sum = 0;

        for(int i = 1; i < map.length - 1; i++) {
            char[] row = map[i];

            for(int j = 1; j < row.length - 1; j++) {
                char c = row[j];
                if(c == '*') {
                    List<Integer> nums = new ArrayList<>();
                    for(int k = j - 1; k < j + 2; k++) {
                        int[] dx = {-1, 1};
                        for(int l : dx) {
                            if(isDigit(map[i][k])) {
                                StringBuilder num = new StringBuilder();
                                for(int m = k; m < row.length && m >= 0 && isDigit(map[i][m]); m += l) {
                                    num.append(map[i][m]);
                                }
                                if(l == -1) {
                                    num.reverse();
                                }
                                nums.add(Integer.parseInt(num.toString()));
                            }
                        }
                    }
                    System.out.println(nums);
                    if(nums.size() == 2) {
                        sum += nums.get(0) * nums.get(1);
                    }
                }
            }
        }
        return sum;
    }

    private boolean isSymbol(char c) {
        return c != '.' && !isDigit(c);
    }
}
                