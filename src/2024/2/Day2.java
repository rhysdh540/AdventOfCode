import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day2 {
	public static Object part1(String input) throws Throwable {
		int validReports = 0;
		for(String reportString : input.split("\n")) {
			List<Integer> list = new ArrayList<>();
			for(String s : reportString.split(" ")) {
				list.add(Integer.parseInt(s));
			}

			int[] report = new int[list.size()];
			for(int i = 0; i < list.size(); i++) {
				report[i] = list.get(i);
			}

			if(isValid(report)) {
				validReports++;
			}
		}

		return validReports;
	}

	public static Object part2(String input) throws Throwable {
		int validReports = 0;
		for(String reportString : input.split("\n")) {
			List<Integer> list = new ArrayList<>();
			for(String s : reportString.split(" ")) {
				list.add(Integer.parseInt(s));
			}

			int[] report = new int[list.size()];
			for(int i = 0; i < list.size(); i++) {
				report[i] = list.get(i);
			}

			if(isValid(report)) {
				validReports++;
				continue;
			}

			for(int i = 0; i < list.size(); i++) {
				int[] copy = new int[report.length - 1];
				System.arraycopy(report, 0, copy, 0, i);
				System.arraycopy(report, i + 1, copy, i, report.length - i - 1);
				if(isValid(copy)) {
					validReports++;
					break;
				}
			}
		}

		return validReports;
	}

	static boolean isValid(int[] report) {
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

	public static void main(String[] args) throws Throwable {
		String input = Files.readString(Paths.get("inputs/2024/2.txt"));
		var o = System.out;
		System.setOut(new PrintStream(new ByteArrayOutputStream()));
		part1(input);part2(input);
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
