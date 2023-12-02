/**
 * <a href="https://adventofcode.com/2023/day/2">Day 2</a>
 */
public class Day2 implements Day.IntDay {
	@Override
	public int run1Int() throws Exception {
		return Main.getInput(2).stream()
				.map(this::getRGB)
				.mapToInt(rgb -> rgb[1] <= 12 && rgb[2] <= 13 && rgb[3] <= 14 ? rgb[0] : 0)
				.sum();
	}

	@Override
	public int run2Int() throws Exception {
		return Main.getInput(2).stream()
				.map(this::getRGB)
				.mapToInt(rgb -> rgb[1] * rgb[2] * rgb[3])
				.sum();
	}

	/**
	 * Gets the maximum amount of red, green, and blue cubes given a string that represents a game
	 * @param s The string, formatted as "Game #: " followed by a list of pulls, separated by "; ".
	 *          Each pull is formatted as "x red, y green, z blue" in a random order, where x, y, and z are integers
	 * @return An array of integers, formatted as [game number, max red, max green, max blue]
	 */
	private int[] getRGB(String s) {
		String line = s.substring(s.indexOf(':') + 2);
		int[] rgb = new int[] { Integer.parseInt(s.substring(5, s.indexOf(':'))), 0, 0, 0 };

		for(String pull : line.split("; ")) {
			for(String str : pull.split(", ")) {
				int num = Integer.parseInt(str.substring(0, str.indexOf(' ')));
				if(str.contains("red") && num > rgb[1]) rgb[1] = num;
				if(str.contains("green") && num > rgb[2]) rgb[2] = num;
				if(str.contains("blue") && num > rgb[3]) rgb[3] = num;
			}
		}
		return rgb;
	}
}
