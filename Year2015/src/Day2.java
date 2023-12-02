import java.util.List;

/**
 * <a href="https://adventofcode.com/2015/day/2">Day 2</a>
 */
public class Day2 implements Day.IntDay {
	@Override
	public int run1Int() throws Exception {
		List<String> input = Main.getInput(2);
		int total = 0;
		for(String s : input) {
			String[] lwh = s.split("x");
			int l = Integer.parseInt(lwh[0]), w = Integer.parseInt(lwh[1]), h = Integer.parseInt(lwh[2]);
			int bottom = l * w, side = w * h, front = h * l;
			int slack = Math.min(Math.min(bottom, side), front);
			total += 2 * bottom + 2 * side + 2 * front + slack;
		}
		return total;
	}

	@Override
	public int run2Int() throws Exception {
		List<String> input = Main.getInput(2);
		int total = 0;
		for(String s : input) {
			String[] lwh = s.split("x");
			int l = Integer.parseInt(lwh[0]), w = Integer.parseInt(lwh[1]), h = Integer.parseInt(lwh[2]);
			int bow = l * w * h;
			int ribbon = 2 * Math.min(Math.min(l + w, w + h), h + l);
			total += ribbon + bow;
		}
		return total;
	}
}
