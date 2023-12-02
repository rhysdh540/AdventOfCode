import java.util.List;

/**
 * <a href="https://adventofcode.com/2023/day/2">Day 2</a>
 */
public class Day2 implements Day.IntDay {
	@Override
	public int run1Int() throws Exception {
		List<String> input = Main.getInput(2);
		int sum = 0;
		for(int i = 1; i < input.size() + 1; i++) {
			int r = 0, g = 0, b = 0;
			String line = input.get(i - 1).substring(input.get(i - 1).indexOf(':') + 2);
			for(String pull : line.split("; ")) {
				for(String s : pull.split(", ")) {
					int num = Integer.parseInt(s.substring(0, s.indexOf(' ')));
					if(s.contains("red")) r = Math.max(r, num);
					if(s.contains("green")) g = Math.max(g, num);
					if(s.contains("blue")) b = Math.max(b, num);
				}
			}
			if(r <= 12 && g <= 13 && b <= 14) sum += i;
		}
		return sum;
	}

	@Override
	public int run2Int() throws Exception {
		List<String> input = Main.getInput(2);
		System.out.println();
		int sum = 0;
		for(int i = 1; i < input.size() + 1; i++) {
			int r = 0, g = 0, b = 0;
			String line = input.get(i - 1).substring(input.get(i - 1).indexOf(':') + 2);
			for(String pull : line.split("; ")) {
				for(String s : pull.split(", ")) {
					int num = Integer.parseInt(s.substring(0, s.indexOf(' ')));
					if(s.contains("red")) r = Math.max(r, num);
					if(s.contains("green")) g = Math.max(g, num);
					if(s.contains("blue")) b = Math.max(b, num);
				}
			}
			sum += r * g * b;
		}
		return sum;
	}
}
