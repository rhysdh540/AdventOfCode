import aoc.Day.IntDay;
import util.Couple;
import util.Utils;

import java.util.List;

/**
* <a href="https://adventofcode.com/2023/day/9">Day 9</a>
*/
public class Day9 extends IntDay {
	@Override
	public int run1Int(List<String> input) {
		int sum = 0;
		for (String line : input) {
			sum += Sequence.create(line).next;
		}
		return sum;
	}

	@Override
	public int run2Int(List<String> input) {
		int sum = 0;
		for (String line : input) {
			sum += Sequence.create(line).prev;
		}
		return sum;
	}

	record Sequence(int[] numbers, int next, int prev) {
		public static Sequence create(String input) {
			String[] split = input.split(" ");
			int[] numbers = new int[split.length];

			for(int i = 0; i < split.length; i++) {
				numbers[i] = Utils.fastParseInt(split[i]);
			}

			Couple<Integer> pair = calculateDifferences(numbers);
			int next = numbers[numbers.length - 1] + pair.second();
			int prev = numbers[0] - pair.first();

			return new Sequence(numbers, next, prev);
		}

		private static Couple<Integer> calculateDifferences(int[] nums) {
			int[] differences = new int[nums.length - 1];
			for (int i = 0; i < nums.length - 1; i++) {
				differences[i] = nums[i + 1] - nums[i];
			}

			boolean all0 = true;
			for(int d : differences) {
				if(d != 0) {
					all0 = false;
					break;
				}
			}
			if(all0) {
				return Couple.create(0, 0);
			}

			Couple<Integer> pair = calculateDifferences(differences);

			int back = differences[0] - pair.first();
			int front = differences[differences.length - 1] + pair.second();

			return Couple.create(back, front);
		}
	}
}
