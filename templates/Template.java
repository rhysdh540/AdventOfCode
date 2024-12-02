import java.nio.file.Files;
import java.nio.file.Paths;

public class Template {
	public static Object part1(String input) throws Throwable {

	}

	public static Object part2(String input) throws Throwable {
		return "Not implemented";
	}

	public static void main(String[] args) throws Throwable {
		// avoid directly refereincing the class name
		String path = String.format("inputs/2024/%s.txt", new Object(){}.getClass().getEnclosingClass().getSimpleName().substring(3));
		String input = Files.readString(Paths.get(path));

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
		System.out.println("-------------------");
	}
}
