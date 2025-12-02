import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (towels, patterns) = sections
    return patterns.split("\n").count { it.matches(Regex("^(${towels.replace(", ", "|")})*")) }
}

private fun PuzzleInput.part2(): Any? {
    val parts = sections
    val towels = parts[0].split(", ").toSet()
    val patterns = parts[1].split("\n")

    val memo = mutableMapOf("" to 1L)

    fun count(p: String): Long = memo.getOrPut(p) {
        towels.filter { p.startsWith(it) }.sumOf { count(p.removePrefix(it)) }
    }

    return patterns.sumOf { count(it) }
}

fun main() {
    val input = PuzzleInput(2024, 19)

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