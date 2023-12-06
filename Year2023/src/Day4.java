import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2023/day/4">Day 4</a>
 */
public class Day4 implements Day.IntDay {
	@Override
	public int run1Int() throws Exception {
		int sum = 0;
		for(String line : Main.getInput(4)) {
			sum += Utils.pow(2, getWinningNums(line) - 1);
		}
		return sum;
	}

	@Override
	public int run2Int() throws Exception {
		List<String> input = Main.getInput(4);
		int[] counts = new int[input.size()];
		Arrays.fill(counts, 1);

		int[] winningNums = new int[10];
		int count = 0;
		for(String s : input) {
			int numWinners = getWinningNums(s);
			if(winningNums.length == count) {
				winningNums = Arrays.copyOf(winningNums, count * 2);
			}
			winningNums[count++] = numWinners;
		}
		winningNums = Arrays.copyOfRange(winningNums, 0, count);

		for (int i = 0; i < input.size() - 1; i++) {
			for(int j = i + 1; j < Math.min(i + winningNums[i] + 1, winningNums.length); j++) {
				counts[j] += counts[i];
			}
		}

		int sum = 0;
		for(int i : counts) sum += i;
		return sum;
	}

	private final Pattern splitPattern = Pattern.compile("([:|])");

	private int getWinningNums(String card) {
		String[] split = splitPattern.split(card);

		List<Integer> list = new ArrayList<>();
		for(String s : Utils.SPACES.split(split[1].trim())) {
			list.add(Utils.fastParseInt(s));
		}

		long count = 0;
		for(String s : Utils.SPACES.split(split[2].trim())) {
			if(list.contains(Utils.fastParseInt(s))) {
				count++;
			}
		}
		return Math.toIntExact(count);
	}
}
