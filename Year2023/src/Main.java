
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	public static void main(String[] args) throws Exception {
		for(int i = 1; i <= 25; i++) {
			try {
				// i love the smell of reflection in the morning
				Day day = (Day) Class.forName("Day" + i).getDeclaredConstructor().newInstance();
				if(i != 1) System.out.println();
				day.printResults();
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
		return Files.readAllLines(Paths.get("Year2023/input/" + day + ".txt"));
	}
}
