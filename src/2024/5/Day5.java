import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day5 {
	public static Object part1(String input) throws Throwable {
		String[] parts = input.split("\n\n");
		String[] rules = parts[0].split("\n");
		String[] sequences = parts[1].split("\n");

		List<List<Integer>> valid = new ArrayList<>();
		for (String seq : sequences) {
			String[] nums = seq.split(",");
			List<Integer> list = new ArrayList<>();
			for (String num : nums) {
				list.add(Integer.parseInt(num));
			}
			valid.add(list);
		}

		for (String rule : rules) {
			String[] nums = rule.split("\\|");
			int n1 = Integer.parseInt(nums[0]);
			int n2 = Integer.parseInt(nums[1]);
			for(int i = 0; i < valid.size(); i++) {
				List<Integer> list = valid.get(i);
				int i1 = list.indexOf(n1);
				int i2 = list.indexOf(n2);
				if(i1 == -1 || i2 == -1) {
					continue;
				}

				if(i1 > i2) {
					valid.remove(list);
					i--;
				}
			}
		}

		int sum = 0;
		for (List<Integer> list : valid) {
			sum += list.get(list.size() / 2);
		}

		return sum;
	}

	public static Object part2(String input) throws Throwable {
		String[] parts = input.split("\n\n");
		String[] rules = parts[0].split("\n");
		String[] sequences = parts[1].split("\n");

		List<List<Integer>> invalid = new ArrayList<>();
		for (String seq : sequences) {
			String[] nums = seq.split(",");
			List<Integer> list = new ArrayList<>();
			for (String num : nums) {
				list.add(Integer.parseInt(num));
			}
			invalid.add(list);
		}

		for(int i = 0; i < invalid.size(); i++) {
			List<Integer> list = invalid.get(i);
			boolean valid = true;
			for(int j = 0; j < rules.length; j++) {
				String rule = rules[j];
				String[] nums = rule.split("\\|");
				int i1 = list.indexOf(Integer.parseInt(nums[0]));
				int i2 = list.indexOf(Integer.parseInt(nums[1]));
				if(i1 == -1 || i2 == -1) {
					continue;
				}
				if(i1 > i2) {
					valid = false;
					Collections.swap(list, i1, i2);
					j = 0; // restart going through rules
				}
			}

			if(valid) {
				invalid.remove(list);
				i--;
			}
		}


		int sum = 0;
		for (List<Integer> list : invalid) {
			sum += list.get(list.size() / 2);
		}

		return sum;
	}

	public static void main(String[] args) throws Throwable {
		String input = Files.readString(Paths.get("inputs/2024/5.txt"));
		var o = System.out;
		System.setOut(new PrintStream(new ByteArrayOutputStream()));
		part1(input); part2(input);
		System.setOut(o);

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
