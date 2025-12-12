import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return sections.dropLast(1).map { it.count('#') }.let { p -> sections.last().lines.count { l -> l.split(" ", limit = 2).let { it[1].split(" ").ints.zip(p).sumOf { (a, b) -> a * b } <= it[0].replace(":", "").split('x').ints.let { (w, h) -> w * h } } } }
}

private fun PuzzleInput.part2(): Any? {
    return "happy advent!"
}

fun main() = PuzzleInput(2025, 12).withSolutions({ part1() }, { part2() }).run()