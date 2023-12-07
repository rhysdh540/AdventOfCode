import java.util.Arrays;
import java.util.List;

/**
 * <a href="https://adventofcode.com/2023/day/6">Day 6</a>
 */
public class Day6 extends Day<Long> {
	@Override
	public Long run1(List<String> input) {
		long[] times = parseInput(input.get(0).substring(5).trim());
		long[] distances = parseInput(input.get(1).substring(9).trim());

		long result = 1;
		for(int i = 0; i < times.length; i++) {
			result *= calculate(times[i], distances[i]);
		}
		return result;
	}

	@Override
	public Long run2(List<String> input) {
		long time = Utils.fastParseLong(input.get(0).substring(5).replace(" ", ""));
		long distance = Utils.fastParseLong(input.get(1).substring(9).replace(" ", ""));
		return calculate(time, distance);
	}

	private long calculate(long time, long distance) {
		double discriminant = Math.sqrt(time * time - 4 * distance);
		long root1 = (long) ((time + discriminant) / 2);
		long root2 = (long) ((time - discriminant) / 2);
		return root1 - root2;
	}

	private long[] parseInput(String input) {
		String[] split = Utils.SPACES.split(input);
		long[] result = new long[10];
		int count = 0;
		for(String s : split) {
			long l = Utils.fastParseLong(s);
			if(result.length == count) result = Arrays.copyOf(result, count * 2);
			result[count++] = l;
		}
		return Arrays.copyOfRange(result, 0, count);
	}
}
