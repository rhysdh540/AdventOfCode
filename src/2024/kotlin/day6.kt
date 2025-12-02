import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val visitedPositions = mutableSetOf<Pair<Int, Int>>()

    var pos = Pair(0, 0)
    o@ for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == '^') {
                pos = Pair(i, j)
                break@o
            }
        }
    }

    var direction = 0

    while (true) {
        visitedPositions.add(pos)

        val next = when (direction) {
            0 -> Pair(pos.first - 1, pos.second)
            1 -> Pair(pos.first, pos.second + 1)
            2 -> Pair(pos.first + 1, pos.second)
            3 -> Pair(pos.first, pos.second - 1)
            else -> error("")
        }

        if (next.first < 0 || next.first >= grid.size || next.second < 0 || next.second >= grid[0].size) {
            break
        }

        if (grid[next.first][next.second] == '#') {
            direction = (direction + 1) % 4
        } else {
            pos = next
        }
    }

    return visitedPositions.size
}

private fun PuzzleInput.part2(): Any? {
    val grid = grid.map { it.toMutableList() }
    val positions = mutableListOf<Pair<Int, Int>>()

    var startPos = Pair(0, 0)
    outer@ for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == '^') {
                startPos = Pair(i, j)
                break@outer
            }
        }
    }

    val directions = listOf(
        Pair(-1, 0),
        Pair(0, 1),
        Pair(1, 0),
        Pair(0, -1)
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
                    val nextPos = Pair(pos.first + directions[direction].first, pos.second + directions[direction].second)
                    if (nextPos.first !in grid.indices || nextPos.second !in grid[0].indices) {
                        break // exit grid
                    }

                    if (grid[nextPos.first][nextPos.second] == '#') {
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

fun main() {
    val input = PuzzleInput(2024, 6)

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