import util.Couple;
import util.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
* <a href="https://adventofcode.com/2023/day/11">Day 11</a>
*/
public class Day11 extends Day<Long> {

	@Override
	public Long run1(List<String> input) {
		return run(input, 1);
	}

	@Override
	public Long run2(List<String> input) {
//		// f(x) = mx + b
//		// x1 = 1, x2 = 2
//		long y1 = run(new ArrayList<>(input), 0); // f(1)
//		long y2 = run1(input); // f(2)
//
//		// calculate f(1,000,000)
//		long m = (y2 - y1); // m = (y2 - y1) / (x2 - x1), but x2 - x1 = 1 so we can ignore it
//		long b = y1 - m; // again should be y1 - m * x1 but x1 = 1
//		return m * 1_000_000 + b;
		return run(input, 1_000_000);
	}

	private Couple<boolean[]> getExpansions(List<String> input) {
		boolean[] hExpansions = new boolean[input.size()];
		boolean[] vExpansions = new boolean[input.get(0).length()];

		for(int i = 0; i < input.size(); i++) {
			if(!input.get(i).contains("#")) {
				hExpansions[i] = true;
			}
		}

		for(int i = 0; i < input.get(0).length(); i++) {
			boolean empty = true;
			for(String s : input) {
				if(s.charAt(i) == '#') {
					empty = false;
					break;
				}
			}
			if(empty) {
				vExpansions[i] = true;
			}
		}

		return Couple.create(hExpansions, vExpansions);
	}

	private long run(List<String> input, int expansionSize) {
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

		Couple<boolean[]> expansions = getExpansions(input);
		boolean[] hExpansions = expansions.first();
		boolean[] vExpansions = expansions.second();

		// calculate sum of manhattan distances
		long sum = 0;
		for(int i = 0; i < galaxyCount; i++) {
			for(int j = i + 1; j < galaxyCount; j++) {
				int x1 = xCoordinates[i], x2 = xCoordinates[j];
				int y1 = yCoordinates[i], y2 = yCoordinates[j];

				int xDiff = Math.abs(x1 - x2);
				int yDiff = Math.abs(y1 - y2);

				if(expansionSize != 0) {
					for(int k = Math.min(x1, x2); k <= Math.max(x1, x2); k++) {
						if(hExpansions[k]) {
							xDiff += expansionSize;
						}
					}
					for(int k = Math.min(y1, y2); k <= Math.max(y1, y2); k++) {
						if(vExpansions[k]) {
							yDiff += expansionSize;
						}
					}
				}

				sum += xDiff + yDiff;
			}
		}
		return sum;
	}
}
