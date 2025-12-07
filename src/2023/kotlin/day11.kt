import dev.rdh.aoc.*
import kotlin.math.abs

private fun PuzzleInput.part1(): Any? {
    return run(boolGrid('#').expandRows().rotateClockwise().expandRows().rotateCounterClockwise())
}

private fun PuzzleInput.part2(): Any? {
    // f(x) = mx + b
    // x1 = 1, x2 = 2
    val y2 = part1() as Int
    val y1 = run(boolGrid('#'))

    // calculate f(1,000,000)
    val m = y2 - y1 // m = (y2 - y1) / (x2 - x1), but x2 - x1 = 1 so we can ignore it
    val b = y1 - m // again should be y1 - m * x1 but x1 = 1
    return m * 1_000_000L + b
}

private fun run(grid: List2d<Boolean>): Int {
    val galaxies = grid.mapIndexed { i, row ->
        row.mapIndexed { j, b -> if (b) v(i, j) else null }.filterNotNull()
    }.flatten()

    // calculate sum of manhattan distances
    return galaxies.indices.sumOf { i ->
        galaxies.drop(i + 1).sumOf { g ->
            abs(galaxies[i].x - g.x) + abs(galaxies[i].y - g.y)
        }
    }
}

private fun Iterable2d<Boolean>.expandRows(): List2d<Boolean> {
    return this.flatMap {
        if (it.anyTrue()) {
            listOf(it.toList())
        } else {
            listOf(it.toList(), it.toList())
        }
    }
}

fun main() = PuzzleInput(2023, 11).withSolutions({ part1() }, { part2() }).run()