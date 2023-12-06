import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2023/day/1">Day 1</a>
 */
public class Day1 implements Day.IntDay {

	@Override
	public int run1Int() throws Exception {
		return doTheThing(Main.getInput(1));
	}

	@Override
	public int run2Int() throws Exception {
		List<String> input = Main.getInput(1);
		// technically we shoudn't include zero but it works anyway
		String[] nums = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
		for(String num : nums) {
			input.replaceAll(s -> {
				// pad replacements with their start and end characters so you don't mess up things like `fiveightwone`
				String toReplace = num.charAt(0) + "" + Arrays.binarySearch(nums, num) + num.charAt(num.length() - 1);
				return s.replace(num, toReplace);
			});
		}
		return doTheThing(input);
	}

	final Pattern number = Pattern.compile("[^0-9]");

	private int doTheThing(List<String> input) {
		int sum = 0;
		for(String s : input) {
			String string = number.matcher(s).replaceAll("");
			int i = Utils.fastParseInt(string.charAt(0) + "" + string.charAt(string.length() - 1));
			sum += i;
		}
		return sum;
	}
}