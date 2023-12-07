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

	public static int parseInt(String s) {
		int i = fastParseInt(s);
		if(!String.valueOf(i).equals(s)) {
			throw new NumberFormatException("For input string: \"" + s + "\"");
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

	public static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

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

	public static void main(String[] args) {
		final int iter = 100_000_000;
		long t1 = 0, t2 = 0;
		for(int i = 0; i < iter; i++) {
			int base = (int)(Math.random() * 10);
			int exp = (int)(Math.random() * 9);
			long start = System.nanoTime();
			int result = pow(base, exp);
			long end = System.nanoTime();
			long start2 = System.nanoTime();
			int result2 = (int) Math.pow(base, exp);
			long end2 = System.nanoTime();
			if(result != result2) {
				System.out.println("Error: " + base + "^" + exp + " = " + result + " != " + result2);
			}
			t1 += end - start;
			t2 += end2 - start2;
		}
		double avg1 = (double) t1 / iter;
		double avg2 = (double) t2 / iter;
		System.out.println("pow: " + avg1 + " ns");
		System.out.println("Math.pow: " + avg2 + " ns");
	}
}
