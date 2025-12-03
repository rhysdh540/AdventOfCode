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

    val ints: List<Int> by lazy {
        lines.map { it.toInt() }
    }

    val chars: List<Char> by lazy {
        input.toList()
    }

    inline fun benchmark(part1: PuzzleInput.() -> Any?, part2: PuzzleInput.() -> Any?, iterations: Int = 1000) {
        val time1 = (1..iterations).sumOf {
            val start = System.nanoTime()
            this.part1()
            System.nanoTime() - start
        }

        val time2 = (1..iterations).sumOf {
            val start = System.nanoTime()
            this.part2()
            System.nanoTime() - start
        }

        println("Benchmark results for $year/$day over $iterations iterations:")
        println("    Part 1: %.2fms".format((time1 / iterations) / 1e6))
        println("    Part 2: %.2fms".format((time2 / iterations) / 1e6))
    }
}
