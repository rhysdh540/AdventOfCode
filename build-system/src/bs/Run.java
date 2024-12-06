package bs;

import java.util.Calendar;

class Run {
	public static void main(String[] args) throws Throwable {
		if(args.length == 0) {
			System.err.println("error: no arguments provided");
			System.err.println("Usage: aoc run <language> <year> <day>");
			System.exit(1);
		}

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int day = -1;

		if(args.length == 2) {
			day = Integer.parseInt(args[1]);
		}

		if(args.length == 3) {
			year = Integer.parseInt(args[1]);
			day = Integer.parseInt(args[2]);

			if(year < 2000) {
				year += 2000;
			}
		}

		if(day == -1) {
			if(cal.get(Calendar.MONTH) != Calendar.DECEMBER) {
				System.err.println("error: day must be specified when not in December");
				System.exit(1);
			}

			day = cal.get(Calendar.DAY_OF_MONTH);
		}

		if(day < 1 || day > 25) {
			System.err.println("error: day " + day + " must be between 1 and 25");
			System.exit(1);
		}

		if(year < 2015 || year > cal.get(Calendar.YEAR)) {
			System.err.println("error: year " + year + " must be between 2015 and " + cal.get(Calendar.YEAR));
			System.exit(1);
		}

		Languages.valueOf(args[0].toUpperCase()).run(year, day);
	}
}