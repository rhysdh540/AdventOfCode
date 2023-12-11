import util.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
* <a href="https://adventofcode.com/2023/day/11">Day 11</a>
*/
public class Day11 extends Day<Long> {
	@Override
	public Long run1(List<String> input) {
		return run(input, 2);
	}

	@Override
	public Long run2(List<String> input) {
		//f(x) = mx + b
		int x1 = 1, x2 = 2;
		long y1 = run(new ArrayList<>(input), x1); // f(1)
		long y2 = run(new ArrayList<>(input), x2); // f(2)

		// calculate f(1,000,000)
		long m = (y2 - y1) / (x2 - x1);
		long b = y1 - m * x1;
		return m * 1_000_000 + b;
	}

	private static final Pattern pattern = Pattern.compile("\\.*");

	/**
	 * @param expansionRate how many lines one blank line turns into
	 */
	private long run(List<String> input, int expansionRate) {
		expansionRate--;
		List<Coordinate> galaxies = new ArrayList<>();
		for(int i = 0; i < input.size(); i++) {
			if(pattern.matcher(input.get(i)).matches()) {
				for(int j = 0; j < expansionRate; j++) {
					input.add(i, input.get(i));
				}
				i += expansionRate;
			}
		}
		String expand = ".".repeat(expansionRate);
		for(int i = 0; i < input.get(0).length(); i++) {
			boolean allEmpty = true;
			for(String s : input) {
				if(s.charAt(i) == '#') {
					allEmpty = false;
					break;
				}
			}
			if(allEmpty) {
				for(int j = 0; j < input.size(); j++) {
					StringBuilder sb = new StringBuilder(input.get(j));
					sb.insert(i, expand);
					input.set(j, sb.toString());
				}
				i += expansionRate;
			}
		}
		for(int i = 0; i < input.size(); i++) {
			for(int j = 0; j < input.get(i).length(); j++) {
				if(input.get(i).charAt(j) == '#') {
					galaxies.add(new Coordinate(i, j));
				}
			}
		}

		long sum = 0;

		for(int i = 0; i < galaxies.size(); i++) {
			for(int j = 0; j < galaxies.size(); j++) {
				if(i == j) continue;
				Coordinate c1 = galaxies.get(i);
				Coordinate c2 = galaxies.get(j);
				int x1 = c1.x(), y1 = c1.y();
				int x2 = c2.x(), y2 = c2.y();
				sum += Math.abs(x1 - x2) + Math.abs(y1 - y2);
			}
		}
		return sum >> 1;
	}
}
