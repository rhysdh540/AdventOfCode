/**
 * <a href="https://adventofcode.com/2015/day/5">Day 5</a>
 */
public class Day5 implements Day.IntDay {
    @Override
    public int run1Int() throws Exception {
		return (int) Main.getInput(5).stream()
                .filter(line -> !line.contains("ab") && !line.contains("cd")
                        && !line.contains("pq") && !line.contains("xy")
                        && line.matches(".*([aeiou].*){3}.*")
                        && line.matches(".*(.)\\1.*"))
                .count();
    }
    
    @Override
    public int run2Int() throws Exception {
        return (int) Main.getInput(5).stream()
                .filter(line -> line.matches(".*(..).*\\1.*")
                        && line.matches(".*(.).\\1.*"))
                .count();
    }
}
                