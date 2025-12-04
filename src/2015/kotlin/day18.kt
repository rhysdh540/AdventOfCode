import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return run(stuckCorners = false)
}

private fun PuzzleInput.part2(): Any? {
    return run(stuckCorners = true)
}

private fun PuzzleInput.run(stuckCorners: Boolean): Int {
    val grid = boolGrid.deepToMutableList()
    val size = grid.size
    val dirs = listOf(
        v(-1, -1), v(-1, 0), v(-1, 1),
        v(0, -1), v(0, 1),
        v(1, -1), v(1, 0), v(1, 1)
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

    return grid.sumOf { it.cardinality() }
}

fun main() = PuzzleInput(2015, 18).withSolutions({ part1() }, { part2() }).run()