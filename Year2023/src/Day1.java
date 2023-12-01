import java.util.List;

public class Day1 {
	public static String run1() throws Exception {
		List<String> input = Main.getInput(1);
		int sum = 0;
		input.replaceAll(s -> s.replaceAll("[^0-9]", ""));
		for(String s : input) {
			sum += Integer.parseInt(s.charAt(0) + "" + s.charAt(s.length() - 1));
		}

		return String.valueOf(sum);
	}

	public static String run2() throws Exception {
		List<String> input = Main.getInput(1);
		int sum = 0;

		for(String in : input) {
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

			String firstNum = in.substring(first);
			String secondNum = in.substring(0, second + 1);
			String result = "";

			if(Character.isDigit(firstNum.charAt(0))) {
				result += firstNum.charAt(0);
			} else {
				result += getNumberStart(firstNum);
			}

			if(Character.isDigit(secondNum.charAt(secondNum.length() - 1))) {
				result += secondNum.charAt(secondNum.length() - 1);
			} else {
				result += getNumberEnd(secondNum);
			}

			sum += Integer.parseInt(result);
		}

		return String.valueOf(sum);
	}

	private static boolean isNumberStart(String s) {
		char c = s.charAt(0);
		return c >= '0' && c <= '9' || s.matches("(zero|one|two|three|four|five|six|seven|eight|nine).*");
	}

	private static boolean isNumberEnd(String s) {
		char c = s.charAt(s.length() - 1);
		return c >= '0' && c <= '9' || s.matches(".*(zero|one|two|three|four|five|six|seven|eight|nine)");
	}

	private static int getNumberStart(String s) {
		if(s.matches("(zero|one|two|three|four|five|six|seven|eight|nine).*")) {
			return switch(s.substring(0, 3)) {
				case "one" -> 1;
				case "two" -> 2;
				case "thr" -> 3;
				case "fou" -> 4;
				case "fiv" -> 5;
				case "six" -> 6;
				case "sev" -> 7;
				case "eig" -> 8;
				case "nin" -> 9;
				case "zer" -> 0;
				default -> throw new AssertionError();
			};
		}
		System.out.println(s);
		throw new AssertionError();
	}

	private static int getNumberEnd(String s) {
		if(s.matches(".*(zero|one|two|three|four|five|six|seven|eight|nine)")) {
			return switch(s.substring(s.length() - 3)) {
				case "one" -> 1;
				case "two" -> 2;
				case "ree" -> 3;
				case "our" -> 4;
				case "ive" -> 5;
				case "six" -> 6;
				case "ven" -> 7;
				case "ght" -> 8;
				case "ine" -> 9;
				case "ero" -> 0;
				default -> throw new AssertionError();
			};
		}
		System.out.println(s);
		throw new AssertionError();
	}
}
