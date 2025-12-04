import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    var santa = v(0, 0)
    val visited = mutableSetOf<Pair<Int, Int>>()
    for (c in input) {
        visited.add(santa)
        santa += move(c)
    }

    return visited.size
}

private fun PuzzleInput.part2(): Any? {
    var santa = v(0, 0)
    var roboSanta = v(0, 0)
    val visited = mutableSetOf<Pair<Int, Int>>()
    for (instructions in input.chunked(2)) {
        visited.add(santa)
        visited.add(roboSanta)
        santa += move(instructions[0])
        if (instructions.length > 1) {
            roboSanta += move(instructions[1])
        }
    }

    return visited.size
}

private fun move(c: Char): Pair<Int, Int> {
    return when (c) {
        'v' -> v(0, 1)
        '>' -> v(1, 0)
        '^' -> v(0, -1)
        '<' -> v(-1, 0)
        else -> throw AssertionError()
    }
}

fun main() = PuzzleInput(2015, 3).withSolutions({ part1() }, { part2() }).run()