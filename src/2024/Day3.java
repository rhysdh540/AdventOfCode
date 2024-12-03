import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class Day3 {
	public static Object part1(String input) throws Throwable {
		Pattern mul = Pattern.compile("mul\\((\\d+),(\\d+)\\)");

		AtomicInteger sum = new AtomicInteger();
		mul.matcher(input).results().forEach(m -> {
			int a = Integer.parseInt(m.group(1));
			int b = Integer.parseInt(m.group(2));
			sum.addAndGet(a * b);
		});

		return sum;
	}

	public static Object part2(String input) throws Throwable {
		Pattern mul = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
		Pattern _do = Pattern.compile("do\\(\\)");
		Pattern dont = Pattern.compile("don't\\(\\)");

		int sum = 0;
		boolean enabled = true;

		for(int i = 0; i < input.length(); i++) {
			if(_do.matcher(input.substring(i)).lookingAt()) {
				enabled = true;
				i += 4;
			} else if(dont.matcher(input.substring(i)).lookingAt()) {
				enabled = false;
				i += 6;
			} else if(enabled && mul.matcher(input.substring(i)).lookingAt()) {
				sum += Integer.parseInt(mul.matcher(input.substring(i)).replaceAll("$1")) * Integer.parseInt(mul.matcher(input.substring(i)).replaceAll("$2"));
			}
		}

		return sum;
	}

	public static void main(String[] args) throws Throwable {
		String input = Files.readString(Paths.get("inputs/2024/3.txt"));

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
