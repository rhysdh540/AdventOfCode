import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public enum Languages {
	JAVA {
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
							"src/" + year + "/Day" + day + ".java",
							"-d", tmp.toAbsolutePath().toString());

			Path java = Path.of(System.getProperty("java.home"), "bin", "java");
			if(!Files.exists(java)) {
				throw new RuntimeException("Java not found in " + java);
			}

			return new String[] {java.toString(), "-cp", tmp.toAbsolutePath().toString(), "Day" + day};
		}
	},
	ZIG {
		@Override
		protected String[] cmd(int year, int day) {
			return new String[] {
					"zig", "run", "src/" + year + "/day" + day + ".zig",
					"-O", "ReleaseSafe",
			};
		}
	},
	;

	protected abstract String[] cmd(int year, int day);

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

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException unchecked(Throwable t) throws T {
		throw (T) t;
	}
}
