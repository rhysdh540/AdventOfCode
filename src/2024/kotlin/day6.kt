import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val visitedPositions = mutableSetOf<Pair<Int, Int>>()

    var pos = run {
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == '^') {
                    return@run v(i, j)
                }
            }
        }
        error("No start position found")
    }

    var direction = 0

    while (true) {
        visitedPositions.add(pos)

        val next = when (direction) {
            0 -> v(pos.x - 1, pos.y)
            1 -> v(pos.x, pos.y + 1)
            2 -> v(pos.x + 1, pos.y)
            3 -> v(pos.x, pos.y - 1)
            else -> error("")
        }

        if (next.x < 0 || next.x >= grid.size || next.y < 0 || next.y >= grid[0].size) {
            break
        }

        if (grid[next.x][next.y] == '#') {
            direction = (direction + 1) % 4
        } else {
            pos = next
        }
    }

    return visitedPositions.size
}

private fun PuzzleInput.part2(): Any? {
    val grid = grid.deepToMutableList()
    val positions = mutableListOf<Pair<Int, Int>>()

    val startPos = run {
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == '^') {
                    return@run v(i, j)
                }
            }
        }
        error("No start position found")
    }

    val directions = listOf(
        v(-1, 0),
        v(0, 1),
        v(1, 0),
        v(0, -1)
    )

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == '.') {
                grid[i][j] = '#'

                var pos = startPos
                var direction = 0
                // store all positions and directions - if we visit a state again, we have a loop
                val visitedStates = mutableSetOf<Pair<Pair<Int, Int>, Int>>()

                while (true) {
                    val state = Pair(pos, direction)
                    if (state in visitedStates) {
                        positions.add(state.first)
                        break
                    }

                    visitedStates.add(state)
                    val nextPos = pos + directions[direction]
                    if (nextPos.x !in grid.indices || nextPos.y !in grid[0].indices) {
                        break // exit grid
                    }

                    if (grid[nextPos.x][nextPos.y] == '#') {
                        direction = (direction + 1) % 4
                    } else {
                        pos = nextPos
                    }
                }

                grid[i][j] = '.'
            }
        }
    }

    return positions.size
}

fun main() = PuzzleInput(2024, 6).withSolutions({ part1() }, { part2() }).run()