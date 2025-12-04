import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return input.count { it == '(' } - input.count { it == ')' }
}

private fun PuzzleInput.part2(): Any? {
    var (floor, i) = v(0, 0)
    for (c in input) {
        floor += if (c == '(') 1 else -1
        if (floor == -1) {
            return i + 1
        }

        i++
    }

    throw AssertionError()
}

fun main() = PuzzleInput(2015, 1).withSolutions({ part1() }, { part2() }).run()