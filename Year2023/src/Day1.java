import aoc.Day.IntDay;
import aoc.Main;
import util.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2023/day/1">Day 1</a>
 */
public class Day1 extends IntDay {
	final Pattern number = Pattern.compile("[^0-9]");

	@Override
	public int run1Int(List<String> input) {
		int sum = 0;
		for(String s : input) {
			String string = number.matcher(s).replaceAll("");
			sum += Utils.fastParseInt(string.charAt(0) + "" + string.charAt(string.length() - 1));
		}
		return sum;
	}

	@Override
	public int run2Int(List<String> input) throws Exception {
		Map<String, String> nums = Map.of(
			"one", "o1e",
			"two", "t2o",     "three", "t3e",
			"four", "f4r",    "five", "f5e",
			"six", "s6x",     "seven", "s7n",
			"eight", "e8t",   "nine", "n9e"
		);
		input.replaceAll(s -> {
			for(Entry<String, String> num : nums.entrySet()) {
				// pad replacements with their start and end characters so you don't mess up things like `fiveightwone`
				s = s.replace(num.getKey(), num.getValue());
			}
			return s;
		});
		return run1(input);
	}
}