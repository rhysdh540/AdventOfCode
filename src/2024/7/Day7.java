import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Day7 {
	public static Object part1(String input) {
		return run(input, false);
	}

	public static Object part2(String input) {
		return run(input, true);
	}

	public static long run(String input, boolean part2) {
		List<Entry<Long, int[]>> equations = input.lines().map(line -> {
			String[] parts = line.split(": ");
			long target = Long.parseLong(parts[0]);
			parts = parts[1].split(" ");
			int[] nums = new int[parts.length];
			for (int i = 0; i < parts.length; i++) {
				nums[i] = Integer.parseInt(parts[i]);
			}

			return Map.entry(target, nums);
		}).toList();

		long sum = 0;
		for (Entry<Long, int[]> equation : equations) {
			if (canSum(equation.getValue(), equation.getKey(), 0, 0, part2)) {
				sum += equation.getKey();
			}
		}

		return sum;
	}

	private static boolean canSum(int[] nums, long target, int index, long currentSum, boolean part2) {
		if(index == nums.length) {
			return currentSum == target;
		}

		if(canSum(nums, target, index + 1, currentSum + nums[index], part2)) {
			return true;
		}

		if(canSum(nums, target, index + 1, currentSum * nums[index], part2)) {
			return true;
		}

		if(part2) {
			int bDigits = (int) (Math.log10(nums[index]) + 1);
			currentSum *= (long) Math.pow(10, bDigits);
			currentSum += nums[index];

			if(canSum(nums, target, index + 1, currentSum, true)) {
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args) throws Throwable {
		String input = Files.readString(Paths.get("inputs/2024/7.txt"));

		long start = System.nanoTime();
		Object result = part1(input);
		long end = System.nanoTime();
		System.out.printf("--- Part 1: %.2fms ---%n", (end - start) / 1e6);
		System.out.println(result);

		start = System.nanoTime();
		result = part2(input);
		end = System.nanoTime();
		System.out.printf("--- Part 2: %.2fms ---%n", (end - start) / 1e6);
		System.out.println(result);
		System.out.println("----------------------");
	}
}
