import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day2 {
	public static void main(String[] args) throws Throwable {
		String path = String.format("inputs/2024/%s.txt", Day2.class.getSimpleName().substring(3));
		String input = Files.readString(Paths.get(path));
		System.out.println("Part 1: " + part1(input));
		System.out.println("Part 2: " + part2(input));
	}

	public static Object part1(String input) throws Throwable {
		int validReports = 0;
		for(String report : input.split("\n")) {
			List<Integer> list = new ArrayList<>();
			for(String s : report.split(" ")) {
				list.add(Integer.parseInt(s));
			}

			if(isValid(list)) {
				validReports++;
			}
		}

		return validReports;
	}

	public static Object part2(String input) throws Throwable {
		int validReports = 0;
		for(String report : input.split("\n")) {
			List<Integer> list = new ArrayList<>();
			for(String s : report.split(" ")) {
				list.add(Integer.parseInt(s));
			}

			if(isValid(list)) {
				validReports++;
				continue;
			}

			for(int i = 0; i < list.size(); i++) {
				List<Integer> copy = new ArrayList<>(list);
				//noinspection RedundantCast
				copy.remove((int) i);
				if(isValid(copy)) {
					validReports++;
					break;
				}
			}
		}

		return validReports;
	}

	static boolean isValid(List<Integer> report) {
		Boolean descending = null;
		Integer prev = null;

		for(int i : report) {
			if(prev == null) {
				prev = i;
				continue;
			}

			if(descending == null) {
				descending = i < prev;
			}

			if(descending != (prev > i)) {
				return false;
			}

			int diff = Math.abs(prev - i);
			if(diff < 1 || diff > 3) {
				return false;
			}

			prev = i;
		}

		return true;
	}
}
