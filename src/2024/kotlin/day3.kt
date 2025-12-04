import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return Regex("""mul\((\d+),(\d+)\)""").findAll(input).map { it.destructured }.sumOf { (a, b) ->
        a.toInt() * b.toInt()
    }
}

private fun PuzzleInput.part2(): Any? {
    return Regex("""(mul|do|don't)\((?:(\d+),(\d+))?\)""").findAll(input).map { it.destructured }
        .fold(0 to true) { (acc, enabled), (op, a, b) ->
            return@fold when (op) {
                "do" -> acc to true
                "don't" -> acc to false
                "mul" -> if (enabled) {
                    acc + a.toInt() * b.toInt() to true
                } else {
                    acc to false
                }

                else -> error("Invalid input")
            }
        }.first
}

fun main() = PuzzleInput(2024, 3).withSolutions({ part1() }, { part2() }).run()