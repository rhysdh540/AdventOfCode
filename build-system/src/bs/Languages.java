package bs;

import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

@SuppressWarnings("resource")
public enum Languages {
	JAVA("Template.java") {
		@Override
		protected String[] cmd(int year, int day) {
			Path tmp;
			String tmpName = "aoc-javac-%d-%d_%s".formatted(year, day, random());
			try {
				tmp = Files.createTempDirectory(tmpName);
			} catch (Exception e) {
				throw unchecked(e);
			}
			ToolProvider.getSystemJavaCompiler()
					.run(null, null, null,
							getSrcFile(year, day).toAbsolutePath().toString(),
							"-d", tmp.toAbsolutePath().toString());

			Path java = Path.of(System.getProperty("java.home"), "bin", "java");
			if(!Files.exists(java)) {
				throw new RuntimeException("Java not found in " + java);
			}

			try {
				Files.walk(tmp).sorted((a, b) -> b.getNameCount() - a.getNameCount()).forEach(p -> {
					if(!Files.isDirectory(p)) {
						p.toFile().deleteOnExit();
					}
				});
			} catch (IOException e) {
				throw unchecked(e);
			}

			return new String[] {java.toString(), "-cp", tmp.toAbsolutePath().toString(), "Day" + day};
		}

		@Override
		protected Path getSrcFile(int year, int day) {
			return Path.of("src", String.valueOf(year), String.valueOf(day), "Day" + day + ".java");
		}
	},

	ZIG("template.zig") {
		public enum Release {
			Debug, ReleaseFast, ReleaseSafe, ReleaseSmall
		}

		@Override
		protected String[] cmd(int year, int day) {
			return new String[] {
					"zig", "run", getSrcFile(year, day).toAbsolutePath().toString(),
					"-O", Release.ReleaseFast.name()
			};
		}

		@Override
		protected Path getSrcFile(int year, int day) {
			return Path.of("src", String.valueOf(year), String.valueOf(day), "day" + day + ".zig");
		}
	},

	KOTLIN("template.kt") {
		@Override
		protected String[] cmd(int year, int day) {
			try {
				Process p = new ProcessBuilder("kotlin", "-version").start();
				p.waitFor();
				if(p.exitValue() != 0) {
					throw new IOException("Kotlin not found in $PATH");
				}
			} catch (Exception e) {
				throw unchecked(e);
			}

			Path tmp;
			String tmpName = "aoc-kotlinc-%d-%d_%s".formatted(year, day, random());
			try {
				tmp = Files.createTempFile(tmpName, ".jar");
			} catch (Exception e) {
				throw unchecked(e);
			}

			try {
				Process kotlinc = new ProcessBuilder("kotlinc", getSrcFile(year, day).toAbsolutePath().toString(),
						"-d", tmp.toAbsolutePath().toString(),
						"-include-runtime"
				).start();

				kotlinc.waitFor();
				if(kotlinc.exitValue() != 0) {
					throw new IOException("kotlinc exited with code " + kotlinc.exitValue());
				}
			} catch (Exception e) {
				throw unchecked(e);
			}

			try {
				Files.walk(tmp).sorted((a, b) -> b.getNameCount() - a.getNameCount()).forEach(p -> {
					if(!Files.isDirectory(p)) {
						p.toFile().deleteOnExit();
					}
				});
			} catch (IOException e) {
				throw unchecked(e);
			}

			return new String[] {"kotlin", tmp.toAbsolutePath().toString()};
		}

		@Override
		protected Path getSrcFile(int year, int day) {
			return Path.of("src", String.valueOf(year), String.valueOf(day), "Day" + day + ".kt");
		}
	},
	;

	private final String templateFileName;

	Languages(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	protected abstract String[] cmd(int year, int day);
	protected abstract Path getSrcFile(int year, int day);

	public void run(int year, int day) {
		try {
			Process p = new ProcessBuilder(cmd(year, day)).inheritIO().start();
			p.waitFor();
			if(p.exitValue() != 0) {
				throw new IOException("Process exited with code " + p.exitValue());
			}
		} catch (Exception e) {
			throw unchecked(e);
		}
	}

	public void init(int year, int day) {
		try {
			String template = Files.readString(Path.of("build-system", "templates", templateFileName));
			template = template.replace("{{year}}", Integer.toString(year));
			template = template.replace("{{day}}", Integer.toString(day));

			Path src = getSrcFile(year, day);
			if(Files.exists(src)) {
				System.out.println("File already exists: " + src);
			} else {
				Files.createDirectories(src.getParent());
				Files.writeString(src, template);
				System.out.println("File created: " + src);
			}

			Path input = getInputFile(year, day);
			if(!Files.exists(input)) {
				Files.createDirectories(input.getParent());
				Files.createFile(input);
				System.out.println("Input file created: " + input);
			}

			if(Files.size(input) == 0) {
				System.out.println("Input file is empty: " + input);

				// if the day is in the future, we don't have access to the input
				// so return early
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("America/New_York")));
				if(year >= cal.get(Calendar.YEAR) && day > cal.get(Calendar.DAY_OF_MONTH)) {
					System.out.printf("Skipping input download because the input for %d/%d is not available yet%n", year, day);
					return;
				}

				String token = Token.get();
				System.out.println("Filling...");

				URL url = URI.create("https://adventofcode.com/%d/day/%d/input".formatted(year, day)).toURL();
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Cookie", "session=" + token);
				conn.connect();

				String content = new String(conn.getInputStream().readAllBytes());
				content = content.substring(0, content.length() - 1); // remove trailing newline
				Files.writeString(input, content);
			}
		} catch (Exception e) {
			throw unchecked(e);
		}
	}

	public static Path getInputFile(int year, int day) {
		return Path.of("inputs", String.valueOf(year), day + ".txt");
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException unchecked(Throwable t) throws T {
		throw (T) t;
	}

	protected static String random() {
		return Long.toHexString(Math.abs(Long.hashCode(System.nanoTime())));
	}
}
