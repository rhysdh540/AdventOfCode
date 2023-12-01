import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	public static void main(String[] args) throws Exception {
		System.out.println("Day 1, Part 1: " + Day1.run1());
		System.out.println("Day 1, Part 2: " + Day1.run2());
	}

	public static List<String> getInput(int day) throws Exception {
		return Files.readAllLines(Paths.get("Year2023/input/" + day + ".txt"));
	}
}
