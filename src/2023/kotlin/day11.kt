import dev.rdh.aoc.*
import kotlin.math.abs

private fun PuzzleInput.part1(): Any? {
    return run(boolGrid.expandRows().rotateClockwise().expandRows().rotateCounterClockwise())
}

private fun PuzzleInput.part2(): Any? {
    // f(x) = mx + b
    // x1 = 1, x2 = 2
    val y2 = part1() as Int
    val y1 = run(boolGrid)

    // calculate f(1,000,000)
    val m = y2 - y1 // m = (y2 - y1) / (x2 - x1), but x2 - x1 = 1 so we can ignore it
    val b = y1 - m // again should be y1 - m * x1 but x1 = 1
    return m * 1_000_000L + b
}

private fun run(grid: List<List<Boolean>>): Int {
    val galaxies = grid.mapIndexed { i, row ->
        row.mapIndexed { j, b -> if (b) Pair(i, j) else null }.filterNotNull()
    }.flatten()

    // calculate sum of manhattan distances
    return galaxies.indices.sumOf { i ->
        galaxies.drop(i + 1).sumOf { g ->
            abs(galaxies[i].first - g.first) + abs(galaxies[i].second - g.second)
        }
    }
}

private fun Iterable<Iterable<Boolean>>.expandRows(): List<List<Boolean>> {
    return this.flatMap {
        if (it.any { it }) {
            listOf(it.toList())
        } else {
            listOf(it.toList(), it.toList())
        }
    }
}

fun main() {
    val input = getInput(2023, 11)

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