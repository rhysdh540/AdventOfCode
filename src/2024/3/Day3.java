import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class Day3 {
	public static Object part1(String input) throws Throwable {
		return Pattern.compile("mul\\((\\d+),(\\d+)\\)").matcher(input).results()
				.mapToInt(m -> Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2))).sum();
	}

	public static Object part2(String input) throws Throwable {
		Pattern pattern = Pattern.compile("(mul|do|don't)\\((?:(\\d+),(\\d+))?\\)");

		AtomicBoolean enabled = new AtomicBoolean(true);
		return pattern.matcher(input).results().mapToInt(m -> {
			switch(m.group(1)) {
				case "do" -> enabled.set(true);
				case "don't" -> enabled.set(false);
				case "mul" -> {
					if(enabled.get()) {
						int a = Integer.parseInt(m.group(2));
						int b = Integer.parseInt(m.group(3));
						return a * b;
					}
				}
			}

			return 0;
		}).sum();
	}

	public static void main(String[] args) throws Throwable {
		String input = Files.readString(Paths.get("inputs/2024/3.txt"));
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
