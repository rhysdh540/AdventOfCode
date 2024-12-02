import java.nio.file.Files;
import java.nio.file.Paths;

public class Template {
	public static void main(String[] args) throws Throwable {
		String path = String.format("inputs/2024/%s.txt", Template.class.getSimpleName().substring(3));
		String input = Files.readString(Paths.get(path));
		System.out.println("Part 1: " + part1(input));
		System.out.println("Part 2: " + part2(input));
	}

	public static Object part1(String input) throws Throwable {

	}

	public static Object part2(String input) throws Throwable {
		return "Not implemented";
	}
}
