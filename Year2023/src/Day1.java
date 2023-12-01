import java.util.List;

/**
 * <a href="https://adventofcode.com/2023/day/1">Day 1</a>
 */
@SuppressWarnings({"unused", "unchecked"})
public class Day1 implements Day {

	@Override
	public Integer run1() throws Exception {
		return doTheThing(Main.getInput(1));
	}

	@Override
	public Integer run2() throws Exception {
		List<String> input = Main.getInput(1),
		// technically we shoudn't include zero but it works anyway
				nums = List.of("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
		for(String num : nums) {
			input.replaceAll(s -> {
				// pad replacements with their start and end characters so you don't mess up things like `fiveightwone`
				String toReplace = num.charAt(0) + String.valueOf(nums.indexOf(num)) + num.charAt(num.length() - 1);
				return s.replaceAll(num, toReplace);
			});
		}
		return doTheThing(input);
	}

	private int doTheThing(List<String> input) {
		return input.stream()
				.map(s -> s.replaceAll("[^0-9]", ""))
				.mapToInt(s -> Integer.parseInt(s.charAt(0) + "" + s.charAt(s.length() - 1)))
				.sum();
	}
}