import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return lines.sumOf {
        val (l, w, h) = it.split("x").ints
        (2 * l * w) + (2 * l * h) + (2 * w * h) + minOf(l * w, l * h, w * h)
    }
}

private fun PuzzleInput.part2(): Any? {
    return lines.sumOf {
        val (l, w, h) = it.split("x").ints
        2 * minOf(l + w, l + h, w + h) + (l * w * h)
    }
}

fun main() = PuzzleInput(2015, 2).withSolutions({ part1() }, { part2() }).run()