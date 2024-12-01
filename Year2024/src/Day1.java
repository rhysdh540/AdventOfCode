import aoc.Day.IntDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* <a href="https://adventofcode.com/2024/day/1">Day 1</a>
*/
public class Day1 extends IntDay {
	@Override
	public int run1Int(List<String> input) {
		List<Integer> first = new ArrayList<>(), second = new ArrayList<>();
		for(String s : input) {
			String[] split = s.split(" {3}");
			first.add(Integer.parseInt(split[0]));
			second.add(Integer.parseInt(split[1]));
		}

		Collections.sort(first);
		Collections.sort(second);

		int sum = 0;
		for(int i = 0; i < first.size(); i++) {
			sum += Math.abs(first.get(i) - second.get(i));
		}

		return sum;
	}

	@Override
	public int run2Int(List<String> input) {
		List<Integer> first = new ArrayList<>(), second = new ArrayList<>();
		for(String s : input) {
			String[] split = s.split(" {3}");
			first.add(Integer.parseInt(split[0]));
			second.add(Integer.parseInt(split[1]));
		}

		int sum = 0;
		for(int i : first) {
			sum += Collections.frequency(second, i) * i;
		}

		return sum;
	}
}
