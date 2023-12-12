package util;

import aoc.Day;

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

	public static void warmup() {
		for(int i = 0; i < 100_000_000; i++) {
			doNothing();
		}
	}

	private static void doNothing() {}

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

	public static void benchmark(Day<?> day) throws Exception {
		System.out.println("Benchmarking " + day.getClass().getSimpleName() + "...");
		warmup();
		int iterations = 100;
		double p1 = 0, p2 = 0;
		for(int i = 0; i < iterations; i++) {
			long start = System.nanoTime();
			day.run1();
			p1 += System.nanoTime() - start;
			start = System.nanoTime();
			day.run2();
			p2 += System.nanoTime() - start;
		}
		p1 /= iterations * 1_000_000;
		p2 /= iterations * 1_000_000;
		System.out.println("Part 1: " + day.run1() + ", took " + p1 + "ms");
		System.out.println("Part 2: " + day.run2() + ", took " + p2 + "ms");
	}
}
