import java.util.List;

/**
 * <a href="https://adventofcode.com/2015/day/5">Day 5</a>
 */
public class Day5 extends Day.IntDay {
    @Override
    public int run1Int(List<String> input) {
		return (int) input.stream()
                .filter(line -> !line.contains("ab") && !line.contains("cd")
                        && !line.contains("pq") && !line.contains("xy")
                        && line.matches(".*([aeiou].*){3}.*")
                        && line.matches(".*(.)\\1.*"))
                .count();
    }

    @Override
    public int run2Int(List<String> input) {
        return (int) input.stream()
                .filter(line -> line.matches(".*(..).*\\1.*")
                        && line.matches(".*(.).\\1.*"))
                .count();
    }
}
