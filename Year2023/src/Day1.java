import java.util.List;

@SuppressWarnings({"unused", "unchecked"})
public class Day1 implements Day {
	static final String start = "(zero|one|two|three|four|five|six|seven|eight|nine).*";
	static final String end = ".*(zero|one|two|three|four|five|six|seven|eight|nine)";

	@Override
	public Integer run1() throws Exception {
		List<String> input = Main.getInput(1);
		int sum = 0;
		input.replaceAll(s -> s.replaceAll("[^0-9]", ""));
		for(String s : input) {
			sum += Integer.parseInt(s.charAt(0) + "" + s.charAt(s.length() - 1));
		}

		return sum;
	}

	@Override
	public Integer run2() throws Exception {
		List<String> input = Main.getInput(1);
		int sum = 0;

		for(String in : input) {
			int[] indices = findFirstAndLastNumberIndices(in);

			String firstNum = in.substring(indices[0]);
			String secondNum = in.substring(0, indices[1] + 1);
			String result = "";

			result += getNumber(firstNum, true);
			result += getNumber(secondNum, false);

			sum += Integer.parseInt(result);
		}

		return sum;
	}

	private int[] findFirstAndLastNumberIndices(String in) {
		int first = 0, second = 0;
		for(int i = 0; i < in.length(); i++) {
			if(isNumberStart(in.substring(i))) {
				first = i;
				break;
			}
		}
		for(int i = in.length() - 1; i >= 0; i--) {
			if(isNumberEnd(in.substring(0, i + 1))) {
				second = i;
				break;
			}
		}
		return new int[]{first, second};
	}

	private String getNumber(String num, boolean isStart) {
		char c = num.charAt(isStart ? 0 : num.length() - 1);
		if(Character.isDigit(c)) {
			return String.valueOf(c);
		} else {
			return String.valueOf(isStart ? getNumberStart(num) : getNumberEnd(num));
		}
	}

	private static boolean isNumberStart(String s) {
		char c = s.charAt(0);
		return c >= '0' && c <= '9' || s.matches(start);
	}

	private static boolean isNumberEnd(String s) {
		char c = s.charAt(s.length() - 1);
		return c >= '0' && c <= '9' || s.matches(end);
	}

	private static int mapStringToNumber(String s) {
		return switch(s) {
	        case "one" -> 1;
	        case "two" -> 2;
	        case "thr", "ree" -> 3;
	        case "fou", "our" -> 4;
	        case "fiv", "ive" -> 5;
	        case "six" -> 6;
	        case "sev", "ven" -> 7;
	        case "eig", "ght" -> 8;
	        case "nin", "ine" -> 9;
	        case "zer", "ero" -> 0;
	        default -> throw new AssertionError();
	    };
	}

	private static int getNumberStart(String s) {
	    if(s.matches(start)) {
	        return mapStringToNumber(s.substring(0, 3));
	    }
	    System.out.println(s);
	    throw new AssertionError();
	}

	private static int getNumberEnd(String s) {
	    if(s.matches(end)) {
	        return mapStringToNumber(s.substring(s.length() - 3));
	    }
	    System.out.println(s);
	    throw new AssertionError();
	}
}