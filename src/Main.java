
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	public static int year;

	public static void main(String[] args) throws Exception {
		try {
			year = Integer.parseInt(args[0]);
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
	 * @throws Exception if {@link Files#readAllLines(java.nio.file.Path) Files.readAllLines} throws an exception
	 */
	public static List<String> getInput(int day) throws Exception {
		return Files.readAllLines(Paths.get("Year" + year + "/input/" + day + ".txt"));
	}

	public static List<String> getInput(int day, String extra) throws Exception {
		return Files.readAllLines(Paths.get("Year" + year + "/input/" + day + "_" + extra + ".txt"));
	}
}
