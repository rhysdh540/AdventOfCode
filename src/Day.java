/**
 * Represents a day in the Advent of Code.
 * <p>
 * Each day has two challenges. The first challenge is run by calling {@link #run1()} and the second challenge is run by calling {@link #run2()}.
 * <p>
 * The input for each day is stored in a file in the <code>input</code> folder. The file name is the day number followed by <code>.txt</code>. For example, the input for day 1 is stored in <code>input/1.txt</code>.
 */
public interface Day<F, S> {

	/**
	 * Runs the first part of the day's challenge.
	 * @return the answer to the first part of the day's challenge
	 * @throws Exception if an error occurs
	 */
	F run1() throws Exception;

	/**
	 * Runs the second part of the day's challenge.
	 * @return the answer to the second part of the day's challenge
	 * @throws Exception if an error occurs
	 */
	S run2() throws Exception;

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
	 */
	default void printResults() {
		System.out.println("\033[1mDay " + getDayNumber() + ":\033[0m");
		System.out.print("  Part 1: ");
		try {
			System.out.println(run1());
		} catch (Throwable e) {
			System.out.println("Exception occurred!\033[91m");
			e.printStackTrace(System.out); // print to sysout instead of syserr
			System.out.print("\033[0m");   // to make sure things are printed in the correct order
		}

		System.out.print("  Part 2: ");
		try {
			System.out.println(run2());
		} catch (Throwable e) {
			System.out.println("Exception occurred!\033[91m");
			e.printStackTrace(System.out);
			System.out.print("\033[0m");
		}
	}
}
