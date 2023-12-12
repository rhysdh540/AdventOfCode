import aoc.Day.LongDay;
import util.Pair;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
* <a href="https://adventofcode.com/2023/day/12">Day 12</a>
*/
public class Day12 extends LongDay {
	@Override
	public long run1Long(List<String> input) {
		List<Pair<String, List<Integer>>> parsed = parseInput(input);
		return parsed.stream()
			.mapToLong(this::arrange)
			.sum();
	}

	@Override
	public long run2Long(List<String> input) {
		throw new UnsupportedOperationException();
	}

	private long arrange(Pair<String, List<Integer>> record) {
		return arrange(record, 0, -1, -1);
	}

	private long arrange(Pair<String, List<Integer>> record, int index, int numsIndex, int numsRemaining) {
		if (index == record.first().length()) {
			return numsRemaining <= 0 && numsIndex == record.getRight().size() - 1 ? 1 : 0;
		}

		char c = record.first().charAt(index);
		long sum = 0;
		if (c == '#' || c == '?') {
			sum += arrangeWithChar(record, '#', index, numsIndex, numsRemaining);
		}
		if (c == '.' || c == '?') {
			sum += arrangeWithChar(record, '.', index, numsIndex, numsRemaining);
		}

		return sum;
	}

	private long arrangeWithChar(Pair<String, List<Integer>> record, char c, int index, int numsIndex, int numsRemaining) {
		if (c == '#') {
			if (numsRemaining == 0) {
				return 0;
			} else if (numsRemaining < 0) {
				return numsIndex == record.second().size() - 1 ? 0 :
					arrange(record, index + 1, numsIndex + 1, record.second().get(numsIndex + 1) - 1);
			} else {
				return arrange(record, index + 1, numsIndex, numsRemaining - 1);
			}
		} else {
			return numsRemaining > 0 ? 0 :
				arrange(record, index + 1, numsIndex, numsRemaining - 1);
		}
	}

	private List<Pair<String, List<Integer>>> parseInput(List<String> input) {
		List<Pair<String, List<Integer>>> parsed = new ArrayList<>();
		for (String line : input) {
			String[] split = line.split(" ");
			String name = split[0];
			List<Integer> values = new ArrayList<>();
			String[] nums = split[1].split(",");
			for (String num : nums) {
				values.add(Utils.fastParseInt(num));
			}
			parsed.add(Pair.of(name, values));
		}
		return parsed;
	}
}
