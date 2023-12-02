/**
 * <a href="https://adventofcode.com/2015/day/1">Day 1</a>
 */
@SuppressWarnings("unused")
public class Day1 implements Day<Integer, Integer> {

	@Override
	public Integer run1() throws Exception {
		String input = Main.getInput(1).get(0);
		return input.replace(")", "").length() - input.replace("(", "").length();
	}

	@Override
	public Integer run2() throws Exception {
		String input = Main.getInput(1).get(0);
		int floor = 0;
		for(int i = 0; i < input.length(); i++) {
			if(input.charAt(i) == '(') floor++;
			else floor--;
			if(floor == -1) return i + 1;
		}
		throw new AssertionError();
	}
}
