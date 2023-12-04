import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <a href="https://adventofcode.com/2023/day/4">Day 4</a>
 */
public class Day4 implements Day.IntDay {
    @Override
    public int run1Int() throws Exception {
		return Main.getInput(4).stream()
                .mapToInt(line -> (int) Math.pow(2, getWinningNums(line) - 1))
                .sum();
    }
    
    @Override
    public int run2Int() throws Exception {
        List<String> input = Main.getInput(4);
        int[] counts = new int[input.size()];
        Arrays.fill(counts, 1);

        int[] winningNums = input.stream()
                .mapToInt(this::getWinningNums)
                .toArray();

        for (int i = 0; i < input.size() - 1; i++) {
            for(int j = i + 1; j < Math.min(i + winningNums[i] + 1, winningNums.length); j++) {
                counts[j] += counts[i];
            }
        }

        return Arrays.stream(counts).sum();
    }

    private int getWinningNums(String card) {
        String[] split = card.split("([:|])");

        List<Integer> winningNums = new ArrayList<>(
                Arrays.stream(split[1].split(" "))
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .toList());
        return (int) Arrays.stream(split[2].split(" "))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .filter(winningNums::contains)
                .count();
    }
}
                