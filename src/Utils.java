import java.util.regex.Pattern;

public class Utils {
	public static final Pattern SPACES = Pattern.compile(" +");


	public static int fastParseInt(String s) {
		int i = 0;
		for(int j = 0; j < s.length(); j++) {
			i = i * 10 + (s.charAt(j) - '0');
		}
		return i;
	}

	public static int fastParseInt(char s) {
		return s - '0';
	}

	public static long fastParseLong(String s) {
		long i = 0;
		for(int j = 0; j < s.length(); j++) {
			i = i * 10 + (s.charAt(j) - '0');
		}
		return i;
	}

	public static long fastParseLong(char s) {
		return s - '0';
	}

	public static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	// idk how this is faster even when Math.pow is intrinsified
	public static int pow(int base, int exp) {
		int result = 1;
		while(exp > 0) {
			if((exp & 1) == 1) {
				result *= base;
			}
			exp >>= 1;
			base *= base;
		}
		return result;
	}
}
