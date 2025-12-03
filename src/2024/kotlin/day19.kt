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

fun main() = PuzzleInput(2024, 19).withSolutions({ part1() }, { part2() }).run()