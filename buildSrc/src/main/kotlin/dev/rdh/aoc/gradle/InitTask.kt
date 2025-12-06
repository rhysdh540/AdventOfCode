package dev.rdh.aoc.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.net.HttpURLConnection
import java.net.URI
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

abstract class InitTask : DefaultTask() {
    @get:Input
    @get:Option(option = "year", description = "The year of the Advent of Code")
    abstract val year: Property<Int>

    @get:Input
    @get:Option(option = "day", description = "The day of the Advent of Code")
    abstract val day: Property<Int>

    @get:Inject
    abstract val layout: ProjectLayout

    init {
        year.convention(now.year)
    }

    @TaskAction
    fun init() {
        year.finalizeValue()
        day.finalizeValue()

        val year = year.get()
        val day = day.get()

        if (year < 2015 || year > now.year) {
            error("year must be between 2015 and ${now.year}")
        }

        if (year < 2025 && day !in 1..25) {
            error("day must be between 1 and 25 before 2025")
        }

        if (year >= 2025 && day !in 1..12) {
            error("day must be between 1 and 12 since 2025")
        }

        val code = code
            .replace("{{year}}", year.toString())
            .replace("{{day}}", day.toString())
            .replace("{{part2msg}}", if (day == 25) "Merry Christmas!" else "Not implemented")

        val file = layout.projectDirectory.file("src/$year/kotlin/day$day.kt").asFile
        if(file.exists() && file.length() != 0L) {
            logger.warn("Source file already exists: $file")
        } else {
            file.parentFile.mkdirs()
            file.writeText(code)

            println("Created file: $file")
        }

        val inFile = layout.projectDirectory.file("src/$year/resources/$day.txt").asFile
        if(inFile.exists() && inFile.length() != 0L) {
            logger.warn("Input file already exists: $inFile")
            return
        }
        // TODO: idk whats going on here
//        if (now < LocalDate.of(year, 12, day).atStartOfDay(ZoneId.of("EST", ZoneId.SHORT_IDS)).toLocalDate()) {
//            logger.warn("Input for $year/$day is not available yet")
//            return
//        }
        inFile.parentFile.mkdirs()
        val url = URI.create("https://adventofcode.com/$year/day/$day/input").toURL()
        val conn = url.openConnection() as HttpURLConnection
        conn.setRequestProperty("Cookie", "session=${aocToken}")
        conn.connect()

        if(conn.responseCode != 200) {
            error("Failed to download input: ${conn.responseCode} ${conn.responseMessage}: ${conn.errorStream.readAllBytes().decodeToString()}")
        }

        // for some reason it comes with a trailing newline
        val content = String(conn.inputStream.readAllBytes()).removeSuffix("\n")
        inFile.writeText(content)
        logger.lifecycle("Downloaded input for $year/$day")
    }

    @get:Internal
    val aocToken: String
        get() {
            layout.projectDirectory.file("token.txt").asFile.let {
                if(it.exists()) return it.readText().trim()
            }

            for(name in listOf("AOC_TOKEN", "AOC_SESSION")) {
                val token = System.getenv(name)
                if(token != null) return token
            }

            for(name in listOf("aocToken", "aocSession")) {
                val token = project.findProperty(name) as? String
                if(token != null) return token
            }

            error("""
                error: no token found
                Please provide your Advent of Code session token in a file named 'token.txt',
                as an environment variable named 'AOC_TOKEN' or 'AOC_SESSION',
                or as a Gradle property named 'aocToken' or 'aocSession'.
                """.trimIndent())
        }
}

private val code = """
import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {

}

private fun PuzzleInput.part2(): Any? {
    return "{{part2msg}}"
}

fun main() = PuzzleInput({{year}}, {{day}}).withSolutions({ part1() }, { part2() }).run()
""".trimIndent()