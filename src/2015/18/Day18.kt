import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_18(input: String): Any? {
    val grid = input.lines().map { it.map { it == '#' }.toBooleanArray() }.toTypedArray()
    return Day18.run(grid)
}

fun part2_18(input: String): Any? {
    val grid = input.lines().map { it.map { it == '#' }.toBooleanArray() }.toTypedArray()
    return Day18.run(grid, true)
}

object Day18 {
    fun run(grid: Array<BooleanArray>, stuckCorners: Boolean = false): Int {
        val size = grid.size
        val dirs = listOf(
            Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
            Pair(0, -1),               Pair(0, 1),
            Pair(1, -1),  Pair(1, 0),  Pair(1, 1)
        )

        if (stuckCorners) {
            grid[0][0] = true
            grid[0][size - 1] = true
            grid[size - 1][0] = true
            grid[size - 1][size - 1] = true
        }

        repeat(100) {
            val newGrid = Array(size) { BooleanArray(size) }
            for (i in 0 until size) {
                for (j in 0 until size) {
                    val count = dirs.count { (dx, dy) ->
                        val x = i + dx
                        val y = j + dy
                        x in 0 until size && y in 0 until size && grid[x][y]
                    }
                    newGrid[i][j] = when {
                        grid[i][j] && count in 2..3 -> true
                        !grid[i][j] && count == 3 -> true
                        else -> false
                    }
                }
            }

            if (stuckCorners) {
                newGrid[0][0] = true
                newGrid[0][size - 1] = true
                newGrid[size - 1][0] = true
                newGrid[size - 1][size - 1] = true
            }

            for (i in 0 until size) {
                for (j in 0 until size) {
                    grid[i][j] = newGrid[i][j]
                }
            }
        }

        return grid.sumOf { it.count { it } }
    }
}

fun main() {
    val input = Path("inputs/2015/18.txt").readText()

    var start = System.nanoTime()
    var result = part1_18(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_18(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}