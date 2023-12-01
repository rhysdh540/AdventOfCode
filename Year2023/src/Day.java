/**
 * Represents a day in the Advent of Code.
 * <p>
 * Each day has two challenges. The first challenge is run by calling {@link #run1()} and the second challenge is run by calling {@link #run2()}.
 * <p>
 * The input for each day is stored in a file in the <code>input</code> folder. The file name is the day number followed by <code>.txt</code>. For example, the input for day 1 is stored in <code>input/1.txt</code>.
 */
public interface Day {

	/**
	 * Runs the first part of the day's challenge.
	 * @return the answer to the first part of the day's challenge
	 * @throws Exception if an error occurs
	 */
	<T> T run1() throws Exception;

	/**
	 * Runs the second part of the day's challenge.
	 * @return the answer to the second part of the day's challenge
	 * @throws Exception if an error occurs
	 */
	<T> T run2() throws Exception;

	/**
	 * Gets the number of the day. Override this method if the class name does not follow the format:
	 * <p>
	 * <code>Day[day number]</code>
	 * @return the number of the day
	 */
	default int getDayNumber() {
		return Integer.parseInt(getClass().getSimpleName().substring(3));
	}

	/**
	 * Prints the results of the day's challenges.
	 * @throws Exception if an error occurs
	 */
	default void printResults() throws Exception {
		System.out.println("Day " + getDayNumber() + ", Part 1: " + run1());
		System.out.println("Day " + getDayNumber() + ", Part 2: " + run2());
	}
}
