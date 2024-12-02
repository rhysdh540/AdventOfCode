import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day1 {
	public static void main(String[] args) throws Throwable {
		String path = String.format("inputs/2024/%s.txt", Day1.class.getSimpleName().substring(3));
		String input = Files.readString(Paths.get(path));
		System.out.println("Part 1: " + part1(input));
		System.out.println("Part 2: " + part2(input));
	}

	public static Object part1(String input) {
		List<Integer> first = new ArrayList<>(), second = new ArrayList<>();
		for(String s : input.split("\n")) {
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

	public static Object part2(String input) {
		List<Integer> first = new ArrayList<>(), second = new ArrayList<>();
		for(String s : input.split("\n")) {
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
