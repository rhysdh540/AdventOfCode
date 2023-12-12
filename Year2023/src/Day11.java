import java.util.ArrayList;
import java.util.List;

/**
* <a href="https://adventofcode.com/2023/day/11">Day 11</a>
*/
public class Day11 extends Day<Long> {

	@Override
	public Long run1(List<String> input) {
		// expand rows
		for(int i = 0; i < input.size(); i++) {
			if(!input.get(i).contains("#")) {
				input.add(i, input.get(i));
				i++;
			}
		}

		// expand columns
		for(int i = 0; i < input.get(0).length(); i++) {
			boolean allEmpty = true;
			for(String s : input) {
				if(s.charAt(i) == '#') {
					allEmpty = false;
					break;
				}
			}
			if(allEmpty) {
				StringBuilder sb;
				for(int k = 0; k < input.size(); k++) {
					sb = new StringBuilder(input.get(k))
						.insert(i, '.');
					input.set(k, sb.toString());
				}
				i++;
			}
		}

		return run(input);
	}

	@Override
	public Long run2(List<String> input) {
		// f(x) = mx + b
		// x1 = 1, x2 = 2
		long y1 = run(new ArrayList<>(input)); // f(1)
		long y2 = run1(input); // f(2)

		// calculate f(1,000,000)
		long m = (y2 - y1); // m = (y2 - y1) / (x2 - x1), but x2 - x1 = 1 so we can ignore it
		long b = y1 - m; // again should be y1 - m * x1 but x1 = 1
		return m * 1_000_000 + b;
	}

	private long run(List<String> input) {
		int size = input.size() * input.get(0).length();
		int[] xCoordinates = new int[size];
		int[] yCoordinates = new int[size];
		int galaxyCount = 0;

		// find all galaxies
		for(int i = 0; i < input.size(); i++) {
			char[] s = input.get(i).toCharArray();
			for(int j = 0; j < s.length; j++) {
				if(s[j] == '#') {
					xCoordinates[galaxyCount] = i;
					yCoordinates[galaxyCount] = j;
					galaxyCount++;
				}
			}
		}

		// calculate sum of manhattan distances
		long sum = 0;
		for(int i = 0; i < galaxyCount; i++) {
			for(int j = i + 1; j < galaxyCount; j++) {
				sum += Math.abs(xCoordinates[i] - xCoordinates[j]) + Math.abs(yCoordinates[i] - yCoordinates[j]);
			}
		}
		return sum;
	}
}
