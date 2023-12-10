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

	public static void main(String[] args) {
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
		if(args.length == 1) {
			System.out.println("Running all days...");
			runAll();
		} else {
			System.out.println("Running day " + args[1] + "...");
			run(Utils.fastParseInt(args[1]));
		}
	}

	private static void runAll() {
		int day = 1;
		while(run(day)) {
			day++;
		}
	}

	/**
	 * run a day
	 * @return does the specified day exist?
	 */
	private static boolean run(int day) {
		try {
			Day<?> dayObj = (Day<?>) Class.forName("Day" + day)
				.getDeclaredConstructor().newInstance();
			dayObj.printResults(true);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	/**
	 * Gets the input for the specified day.
	 * @param day the day number
	 * @return A list of strings representing the input for the specified day
	 * @throws FileNotFoundException if the input file for the specified day does not exist
	 * @throws RuntimeException if {@link Files#readAllLines(java.nio.file.Path) Files.readAllLines} throws an exception
	 */
	public static List<String> getInput(int day) {
		return getInput("Year" + year + "/input/" + day + ".txt");
	}

	public static List<String> getInput(int day, String extra) {
		return getInput("Year" + year + "/input/" + day + "_" + extra + ".txt");
	}

	public static List<String> getInput(String path) {
		Path file = Path.of(path);
		if(!Files.exists(file)) {
			throw new RuntimeException(new FileNotFoundException("Input not found for path " + file.toFile().getAbsolutePath()));
		}
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		return lines;
	}
}
