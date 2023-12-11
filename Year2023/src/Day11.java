import java.util.ArrayList;
import java.util.List;

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
		int x1 = 2;
		int x2 = 10;
		long y1 = run(new ArrayList<>(input), x1);
		long y2 = run(new ArrayList<>(input), x2);

		//calculate line equation
		//where x = 1,000,000
		long m = (y2 - y1) / (x2 - x1);
		long b = y1 - m * x1;
		return m * 1000000 + b;
	}

	/**
	 * @param expansionRate how many lines one blank line turns into
	 */
	private long run(List<String> input, int expansionRate) {
		List<Pair<Integer, Integer>> galaxies = new ArrayList<>();
		for(int i = 0; i < input.size(); i++) {
			if(input.get(i).matches("\\.*")) {
				for(int j = 1; j < expansionRate; j++) {
					input.add(i, input.get(i));
				}
				i += expansionRate - 1;
			}
		}
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
					input.set(j, input.get(j).substring(0, i) + ".".repeat(expansionRate - 1) + input.get(j).substring(i));
				}
				i += expansionRate - 1;
			}
		}
		for(int i = 0; i < input.size(); i++) {
			for(int j = 0; j < input.get(i).length(); j++) {
				if(input.get(i).charAt(j) == '#') {
					galaxies.add(Pair.of(i, j));
				}
			}
		}

		long sum = 0;

		for(int i = 0; i < galaxies.size(); i++) {
			for(int j = 0; j < galaxies.size(); j++) {
				if(i == j) continue;
				int x1 = galaxies.get(i).getLeft();
				int y1 = galaxies.get(i).getRight();
				int x2 = galaxies.get(j).getLeft();
				int y2 = galaxies.get(j).getRight();
				sum += Math.abs(x1 - x2) + Math.abs(y1 - y2);
			}
		}
		return sum / 2;
	}
}
