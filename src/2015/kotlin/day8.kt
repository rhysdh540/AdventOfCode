import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val rHex = Regex("""\\x[0-9a-f]{2}""")
    return lines.sumOf {
        val mem = it.replace("\\\\", "x")
            .replace("\\\"", "x")
            .replace(rHex, "x")
        it.length - (mem.length - 2)
    }
}

private fun PuzzleInput.part2(): Any? {
    return lines.sumOf { l -> l.count { it in "\\\"" } + 2 }
}

fun main() = PuzzleInput(2015, 8).withSolutions({ part1() }, { part2() }).run()