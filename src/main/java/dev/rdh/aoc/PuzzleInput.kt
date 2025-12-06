@file:Suppress("NOTHING_TO_INLINE", "unused")

package dev.rdh.aoc

class PuzzleInput private constructor(val year: Int, val day: Int, val input: String) {
    constructor(year: Int, day: Int) : this(year, day, Unit.run {
        val callerClass = Class.forName(Thread.currentThread().stackTrace[2].className)
        val resourceName = callerClass.getResource("$day.txt") ?: error("Input file not found for $year/$day")
        resourceName.readText()
    })

    constructor(input: String) : this(-1, -1, input)

    val lines: List<String> by lazy {
        splitBy("\n")
    }

    fun splitBy(delimiter: String): List<String> {
        return input.split(delimiter)
    }

    val sections by lazy { splitBy(blankLines) }

    val charGrid: List<List<Char>> by lazy {
        lines.map { it.toList() }
    }

    val grid by lazy { charGrid }

    fun boolGrid(trueChar: Char = '#'): List<List<Boolean>> {
        return charGrid.map { row -> row.map { it == trueChar } }
    }

    val boolGrid: List<List<Boolean>> by lazy {
        boolGrid()
    }

    val intGrid: List<List<Int>> by lazy {
        charGrid.map { row -> row.map { it.digitToInt() } }
    }

    fun intGridOr(default: Int): List<List<Int>> {
        return charGrid.map { row -> row.map { it.digitToIntOrNull() ?: default } }
    }

    val chars: List<Char> by lazy {
        input.toList()
    }

    fun withSolutions(
        part1: PuzzleInput.() -> Any?,
        part2: PuzzleInput.() -> Any?
    ): Solution {
        return Solution(this, part1, part2)
    }
}

class Solution(val input: PuzzleInput,
               private val part1: PuzzleInput.() -> Any?,
               private val part2: PuzzleInput.() -> Any?) {
    fun part1() = input.part1()
    fun part2() = input.part2()

    fun run() {
        var start = System.nanoTime()
        var result = part1()
        var end = System.nanoTime()
        println("Advent of Code ${input.year}/${input.day}")
        println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
        println(result)

        start = System.nanoTime()
        result = part2()
        end = System.nanoTime()
        println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
        println(result)
        println("----------------------")
    }

    fun benchmark(
        targetTimeMs: Long = 500,
        maxIterations: Int = 1_000_000,
        p2TargetTimeMs: Long = targetTimeMs
    ) {
        val p1 = Benchmark(
            name = "Part 1",
            targetTimeNs = targetTimeMs * 1_000_000,
            maxIterations = maxIterations,
            fn = { part1() }
        ).bench()

        val p2 = Benchmark(
            name = "Part 2",
            targetTimeNs = p2TargetTimeMs * 1_000_000,
            maxIterations = maxIterations,
            fn = { part2() }
        ).bench()

        println("Benchmark results for ${input.year}/${input.day}:\n    $p1\n    $p2" )
    }
}
