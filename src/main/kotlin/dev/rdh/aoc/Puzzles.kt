package dev.rdh.aoc

class PuzzleInput(raw: String) {
    val input = raw
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
        raw.toList()
    }
}

fun getInput(year: Int, day: Int): PuzzleInput {
    val callerClass = Class.forName(Thread.currentThread().stackTrace[2].className)
    val resourceName = callerClass.getResource("$day.txt") ?: error("Input file not found for $year/$day")
    return PuzzleInput(resourceName.readText())
}
