package dev.rdh.aoc.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.intellij.lang.annotations.Language

abstract class InitTask : DefaultTask() {
    @get:Input
    @get:Option(option = "year", description = "The year of the Advent of Code")
    abstract val year: Property<Int>

    @get:Input
    @get:Option(option = "day", description = "The day of the Advent of Code")
    abstract val day: Property<Int>

    @TaskAction
    fun init() {
        year.finalizeValue()
        day.finalizeValue()

        val year = year.get()
        val day = day.get()

        val code = code
            .replace("{{year}}", year.toString())
            .replace("{{day}}", day.toString())

        val file = project.file("src/$year/kotlin/day$day.kt")
        if(file.exists()) {
            error("File already exists: $file")
        }

        file.parentFile.mkdirs()
        file.writeText(code)

        println("Created file: $file")
    }
}

@Language("kotlin")
private val code = """
import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {

}

private fun PuzzleInput.part2(): Any? {
    return "Not implemented"
}

fun main() {
    val input = getInput({{year}}, {{day}})

    var start = System.nanoTime()
    var result = input.part1()
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = input.part2()
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}
""".trimIndent()