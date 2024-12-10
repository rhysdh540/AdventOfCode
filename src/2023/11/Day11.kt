import Day11.expandRows
import Day11.rotateClockwise
import Day11.rotateCounterClockwise
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

fun part1_11(input: String): Any? {
    var grid = input.lines().map { it.toCharArray() }
        .expandRows().rotateClockwise()
        .expandRows().rotateCounterClockwise()
        .toTypedArray()

    return Day11.run(grid)
}

fun part2_11(input: String): Any? {
    // f(x) = mx + b
    // x1 = 1, x2 = 2
    val y2 = part1_11(input) as Int
    val y1 = Day11.run(input.lines().map { it.toCharArray() }.toTypedArray())

    // calculate f(1,000,000)
    val m = y2 - y1 // m = (y2 - y1) / (x2 - x1), but x2 - x1 = 1 so we can ignore it
    val b = y1 - m // again should be y1 - m * x1 but x1 = 1
    return m * 1_000_000L + b
}

object Day11 {
    fun Iterable<CharArray>.expandRows(): List<CharArray> {
        return this.flatMap {
            if ('#' in it) {
                listOf(it)
            } else {
                listOf(it, it)
            }
        }
    }

    fun Iterable<CharArray>.rotateClockwise(): List<CharArray> {
        val l = this.toList()
        val numRows = l.size
        val numCols = l[0].size
        val rotated = Array(numCols) { CharArray(numRows) }
        for (i in l.indices) {
            for (j in l[i].indices) {
                rotated[j][numRows - 1 - i] = l[i][j]
            }
        }
        return rotated.toList()
    }

    fun Iterable<CharArray>.rotateCounterClockwise(): List<CharArray> {
        val l = this.toList()
        val numRows = l.size
        val numCols = l[0].size
        val rotated = Array(numCols) { CharArray(numRows) }
        for (i in l.indices) {
            for (j in l[i].indices) {
                rotated[numCols - 1 - j][i] = l[i][j]
            }
        }
        return rotated.toList()
    }

    fun run(grid: Array<CharArray>): Int {
        val galaxies = grid.mapIndexed { i, row ->
            row.mapIndexed { j, c -> if (c == '#') Pair(i, j) else null }.filterNotNull()
        }.flatten()

        // calculate sum of manhattan distances
        return galaxies.indices.sumOf { i ->
            galaxies.drop(i + 1).sumOf { g ->
                abs(galaxies[i].first - g.first) + abs(galaxies[i].second - g.second)
            }
        }
    }
}

fun main() {
    val input = Path("inputs/2023/11.txt").readText()

    var start = System.nanoTime()
    var result = part1_11(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_11(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}