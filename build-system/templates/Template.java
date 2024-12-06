import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.PrintStream;
import java.io.OutputStream;

public class Day{{day}} {
	public static Object part1(String input) throws Throwable {

	}

	public static Object part2(String input) throws Throwable {
		return "Not implemented";
	}

	public static void main(String[] args) throws Throwable {
		String input = Files.readString(Paths.get("inputs/{{year}}/{{day}}.txt"));
		var o = System.out;
		System.setOut(new PrintStream(OutputStream.nullOutputStream()));
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
