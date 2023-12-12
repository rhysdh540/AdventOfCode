import aoc.Day.LongDay;
import util.Pair;
import util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <a href="https://adventofcode.com/2023/day/12">Day 12</a>
*/
public class Day12 extends LongDay {
	@Override
	public long run1Long(List<String> input) {
		List<Pair<String, List<Integer>>> parsed = parseInput(input);
		long sum = 0L;
		for(var record : parsed) {
			sum += arrange(record);
		}
		return sum;
	}

	@Override
	public long run2Long(List<String> input) {
		List<Pair<String, List<Integer>>> parsed = parseInput(input);
		long sum = 0L;
		for(var record : parsed) {
			sum += arrange(repeat(record));
		}
		return sum;
	}

	private String repeat(String s) {
		String result = (s + "?").repeat(5);
		return result.substring(0, result.length() - 1);
	}

	private List<Integer> repeat(List<Integer> nums) {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			list.addAll(nums);
		}
		return list;
	}

	private Pair<String, List<Integer>> repeat(Pair<String, List<Integer>> record) {
		return Pair.of(repeat(record.first()), repeat(record.second()));
	}

	private long arrange(Pair<String, List<Integer>> record) {
		return arrange(record, 0, -1, -1);
	}

	record Input(Pair<String, List<Integer>> record, int index, int numsIndex, int numsRemaining) {
		@Override
		public int hashCode() {
			return record.first().hashCode() * 31 + index * 31 + numsIndex * 31 + numsRemaining;
		}
	}

	private final Map<Input, Long> cache = new HashMap<>();

	private long arrange(Pair<String, List<Integer>> record, int index, int numsIndex, int numsRemaining) {
		Input input = new Input(record, index, numsIndex, numsRemaining);
		if(cache.containsKey(input)) {
			return cache.get(input);
		}
		if (index == record.first().length()) {
			return numsRemaining <= 0 && numsIndex == record.getRight().size() - 1 ? 1 : 0;
		}

		char c = record.first().charAt(index);
		long sum = 0;
		if(c == '?') {
			sum += arrangeWithChar(record, '#', index, numsIndex, numsRemaining);
			sum += arrangeWithChar(record, '.', index, numsIndex, numsRemaining);
		} else {
			sum += arrangeWithChar(record, c, index, numsIndex, numsRemaining);
		}

		cache.put(input, sum);
		return sum;
	}

	private long arrangeWithChar(Pair<String, List<Integer>> record, char c, int index, int numsIndex, int numsRemaining) {
		if (c == '#') {
			if (numsRemaining == 0) { // no more # left
				return 0;
			} else if (numsRemaining < 0) {
				if(numsIndex == record.second().size() - 1) {
					return 0;
				} else {
					return arrange(record, index + 1, numsIndex + 1, record.second().get(numsIndex + 1) - 1);
				}
			} else {
				return arrange(record, index + 1, numsIndex, numsRemaining - 1);
			}
		} else { // c == '.'
			if(numsRemaining > 0) {
				return 0;
			} else {
				return arrange(record, index + 1, numsIndex, numsRemaining - 1);
			}
		}
	}

	private List<Pair<String, List<Integer>>> parseInput(List<String> input) {
		List<Pair<String, List<Integer>>> parsed = new ArrayList<>();
		for (String line : input) {
			String[] split = line.split(" ");
			List<Integer> values = new ArrayList<>();
			for (String num : split[1].split(",")) {
				values.add(Utils.fastParseInt(num));
			}
			parsed.add(Pair.of(split[0], values));
		}
		return parsed;
	}
}
