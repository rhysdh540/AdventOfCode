import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static int year;

	public static void main(String[] args) throws Exception {
		try {
			year = Utils.fastParseInt(args[0]);
			if(!Files.exists(Paths.get("Year" + year))) {
				throw new FileNotFoundException();
			}
		} catch (NumberFormatException | FileNotFoundException e) {
			System.err.println("Please specify a valid year!");
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Please specify a year!");
			return;
		}

		System.out.println("\033[1;4mAdvent Of Code\033[0;36;1m " + year + "\033[0m");
		System.out.println("https://adventofcode.com/" + year);

		for(int i = 1; i <= 25; i++) {
			try {
				// i love the smell of reflection in the morning
				Day<?> day = (Day<?>) Class.forName("Day" + i).getDeclaredConstructor().newInstance();
				day.printResults(true);
			} catch(ClassNotFoundException e) {
				return;
			}
		}
	}

	/**
	 * Gets the input for the specified day.
	 * @param day the day number
	 * @return A list of strings representing the input for the specified day
	 * @throws FileNotFoundException if the input file for the specified day does not exist
	 * @throws Exception if {@link Files#readAllLines(java.nio.file.Path) Files.readAllLines} throws an exception
	 */
	public static List<String> getInput(int day) throws Exception {
		return getInput("Year" + year + "/input/" + day + ".txt");
	}

	public static List<String> getInput(int day, String extra) throws Exception {
		return getInput("Year" + year + "/input/" + day + "_" + extra + ".txt");
	}

	public static List<String> getInput(String path) throws Exception {
		Path file = Path.of(path);
		if(!Files.exists(file)) {
			throw new FileNotFoundException("Input not found for path " + file.toFile().getAbsolutePath());
		}
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		}
		return lines;
	}
}
