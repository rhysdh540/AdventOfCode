import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public enum Languages {
	JAVA("Template.java") {
		@Override
		protected String[] cmd(int year, int day) {
			Path tmp;
			String tmpName = "aoc-out-%d-%d_%s".formatted(year, day, Long.toHexString(Math.abs(Long.hashCode(System.nanoTime()))));
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

			return new String[] {java.toString(), "-cp", tmp.toAbsolutePath().toString(), "Day" + day};
		}

		@Override
		protected Path getSrcFile(int year, int day) {
			return Path.of("src", String.valueOf(year), "Day" + day + ".java");
		}
	},
	ZIG("template.zig") {
		@Override
		protected String[] cmd(int year, int day) {
			return new String[] {
					"zig", "run", getSrcFile(year, day).toAbsolutePath().toString(),
					"-O", "ReleaseSafe",
			};
		}

		@Override
		protected Path getSrcFile(int year, int day) {
			return Path.of("src", String.valueOf(year), "day" + day + ".zig");
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
				throw new IOException("File already exists: " + src);
			}

			Files.writeString(src, template);

			System.out.println("File created: " + src);

			Path input = getInputFile(year, day);
			if(!Files.exists(input)) {
				Files.createDirectories(input.getParent());
				Files.createFile(input);
				System.out.println("Input file created: " + input);
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
}
