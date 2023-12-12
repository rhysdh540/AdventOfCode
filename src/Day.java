import util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a day in the Advent of Code.
 * <p>
 * Each day has two challenges.
 * The first challenge is run by calling {@link #run1} and the second challenge is run by calling {@link #run2}.
 * <p>
 * The input for each day is stored in a file in the <code>input</code> folder.
 * The file name is the day number followed by <code>.txt</code>.
 * For example, the input for day 1 is stored in <code>input/1.txt</code>.
 * @param <T> the type of the answer to the day's challenges
 */
public abstract class Day<T> {

	/**
	 * Runs the first part of the day's challenge.
	 * @return the answer to the first part of the day's challenge
	 * @throws Exception if an error occurs
	 */
	public abstract T run1(List<String> input) throws Exception;

	/**
	 * Runs the second part of the day's challenge.
	 * @return the answer to the second part of the day's challenge
	 * @throws Exception if an error occurs
	 */
	public abstract T run2(List<String> input) throws Exception;

	public final T run1() throws Exception {
		return run1(getInput());
	}

	public final T run2() throws Exception {
		return run2(getInput());
	}

	protected final int dayNumber = makeDayNumber();

	/**
	 * Gets the number of the day.
	 * Override this method if the class name does not follow the format:
	 * <p>
	 * <code>Day[day number]</code>
	 * @return the number of the day
	 */
	private int makeDayNumber() {
		return Utils.parseInt(getClass().getSimpleName().substring(3));
	}

	/**
	 * Prints the results of the day's challenges.
	 */
	public void printResults(boolean forceLogTime) {
		System.out.println("\033[1mDay " + dayNumber + ":\033[0m");
		time(1, this::run1, forceLogTime);
		time(2, this::run2, forceLogTime);
	}

	@FunctionalInterface
	interface RunMethod<T> {
		T run(List<String> input) throws Exception;
	}

	private void time(int num, RunMethod<T> runnable, boolean forceLogTime) {
		System.out.print("  Part " + num + ": ");
		try {
			List<String> input = getInput();
			long start = System.nanoTime();
			T result = runnable.run(input);
			long end = System.nanoTime();
			System.out.println(result);
			if(end - start >= 1_000_000_000) {
				System.out.printf("    Time: \033[91m%,.3fs\033[0m%n", (end - start) / 1_000_000_000.0);
			} else if(forceLogTime) {
				System.out.printf("    Time: \033[96m%,.3fms\033[0m%n", (end - start) / 1_000_000.0);
			}
		} catch (Throwable e) {
			System.out.println("Exception occurred!\033[91m");
			e.printStackTrace(System.out); // Print the stack trace to sysout so things are printed in the correct order
			System.out.print("\033[0m");
		}
	}

	private final List<String> input; {
		try {
			input = Main.getInput(dayNumber);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected final List<String> getInput() {
		return new ArrayList<>(input);
	}

	public static abstract class IntDay extends Day<Integer> {
		@Override
		public Integer run1(List<String> input) throws Exception {
			return run1Int(input);
		}

		@Override
		public Integer run2(List<String> input) throws Exception {
			return run2Int(input);
		}

		public abstract int run1Int(List<String> input) throws Exception;

		public abstract int run2Int(List<String> input) throws Exception;
	}
}
