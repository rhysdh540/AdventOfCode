package bs;

import java.nio.file.Files;
import java.nio.file.Path;

public class Token {
	public static String get() {
		Path path = Path.of("token.txt");
		if(Files.exists(path)) {
			try {
				return Files.readString(path).strip();
			} catch(Throwable e) {
				throw Languages.unchecked(e);
			}
		}

		String[] envNames = {"AOC_SESSION", "AOC_TOKEN"};
		for(String envName : envNames) {
			String token = System.getenv(envName);
			if(token != null) {
				return token.strip();
			}
		}

		throw new IllegalStateException("""
				error: no token found
				Please provide your Advent of Code session token in a file named 'token.txt'
				or as an environment variable named 'AOC_SESSION' or 'AOC_TOKEN'""");
	}
}
