package util;

import java.util.regex.Pattern;

public final class Utils {
	public static final Pattern SPACES = Pattern.compile(" +");

	public static int fastParseInt(String s) {
		if(s.charAt(0) == '-') {
			return -fastParseInt(s.substring(1));
		}
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

	public static int countMatches(String s, String m) {
		int count = 0;
		int index = 0;
		while((index = s.indexOf(m, index)) != -1) {
			count++;
			index += m.length();
		}
		return count;
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

	public static int sum(int[] arr) {
		int sum = 0;
		for(int i : arr) {
			sum += i;
		}
		return sum;
	}

	public static long sum(long[] arr) {
		long sum = 0;
		for(long i : arr) {
			sum += i;
		}
		return sum;
	}

	public static double sum(double[] arr) {
		double sum = 0;
		for(double i : arr) {
			sum += i;
		}
		return sum;
	}

	public static int product(int[] arr) {
		int product = 1;
		for(int i : arr) {
			product *= i;
		}
		return product;
	}

	public static long product(long[] arr) {
		long product = 1;
		for(long i : arr) {
			product *= i;
		}
		return product;
	}

	public static double product(double[] arr) {
		double product = 1;
		for(double i : arr) {
			product *= i;
		}
		return product;
	}

	public static int max(int[] arr) {
		int max = Integer.MIN_VALUE;
		for(int i : arr) {
			if(i > max) {
				max = i;
			}
		}
		return max;
	}

	public static long max(long[] arr) {
		long max = Long.MIN_VALUE;
		for(long i : arr) {
			if(i > max) {
				max = i;
			}
		}
		return max;
	}

	public static double max(double[] arr) {
		double max = Double.MIN_VALUE;
		for(double i : arr) {
			if(i > max) {
				max = i;
			}
		}
		return max;
	}

	public static int min(int[] arr) {
		int min = Integer.MAX_VALUE;
		for(int i : arr) {
			if(i < min) {
				min = i;
			}
		}
		return min;
	}

	public static long min(long[] arr) {
		long min = Long.MAX_VALUE;
		for(long i : arr) {
			if(i < min) {
				min = i;
			}
		}
		return min;
	}

	public static double min(double[] arr) {
		double min = Double.MAX_VALUE;
		for(double i : arr) {
			if(i < min) {
				min = i;
			}
		}
		return min;
	}

	public static long lcm(long a, long b) {
		long gcd = gcd(a, b);
		if(gcd == 0) return 0;
		return a * b / gcd;
	}

	public static <T> void reverse(T[] arr) {
		for(int i = 0, j = arr.length - 1; i < j; i++, j--) {
			T temp = arr[i];
			arr[i] = arr[j];
			arr[j] = temp;
		}
	}

	public static long gcd(long a, long b) {
		if(a == 0) return b;
		if(b == 0) return a;

		long k = 0;
		while((a & 1) == 0 && (b & 1) == 0) {
			a >>= 1;
			b >>= 1;
			k++;
		}

		while((a & 1) == 0) {
			a >>= 1;
		}

		do {
			while((b & 1) == 0) {
				b >>= 1;
			}
			if(a > b) {
				long t = a;
				a = b;
				b = t;
			}
			b -= a;
		} while(b != 0);

		return a << k;
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
