plugins {
	id("java")
}

allprojects {
    apply(plugin = "java")
    sourceSets["main"].java {
        srcDir("src")
    }
}

subprojects {
	project.ext["year"] = project.name.substring(4)
    dependencies {
        implementation(rootProject)
    }

    tasks.register<JavaExec>("run") {
		group = "aoc"
        classpath = sourceSets["main"].runtimeClasspath
        mainClass = "aoc.Main"
        args = listOf(project.ext["year"].toString())
        workingDir = rootDir
        jvmArgs = "-Xmx8G -Xms8G -Xss1024m -XX:+UnlockExperimentalVMOptions -XX:+UseCriticalJavaThreadPriority -XX:+UseZGC -XX:AllocatePrefetchStyle=1 -XX:-ZProactive".split(" ").toList()
    }

	tasks.compileJava {
		doFirst {
			print("Compiling ${project.name}...")
		}
		doLast {
			println(" done!")
		}
	}

    tasks.register("initDay") {
		group = "aoc"
        doLast {
            var day: Int? = null
			for(i in 1..25) {
				if(!project.file("src/Day${i}.java").exists()) {
					day = i
					break
				}
			}
			if(day == null) {
				throw IllegalStateException("All days are already implemented!")
			}
            val source = project.file("src/Day${day}.java")
			source.parentFile.mkdirs()
			source.createNewFile()
			source.writeText("""import aoc.Day.IntDay;

import java.util.List;

/**
* <a href="https://adventofcode.com/${project.ext["year"]}/day/${day}">Day ${day}</a>
*/
public class Day${day} extends IntDay {
	@Override
	public int run1Int(List<String> input) {

	}

	@Override
	public int run2Int(List<String> input) {
		throw new UnsupportedOperationException();
	}
}
			""".stripIndent())
			project.file("input/${day}.txt").createNewFile()
        }
    }
}

val Project.sourceSets
	get() = extensions.getByType(SourceSetContainer::class.java)