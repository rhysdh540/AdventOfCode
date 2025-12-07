import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val grid = grid
    val start = run {
        var s = v(0, 0)
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] == 'S') {
                    s = v(x, y)
                }
            }
        }
        s
    }

    val queue = ArrayDeque<Vec2i>()
    val found = mutableSetOf<Vec2i>()
    val visited = mutableSetOf<Vec2i>()
    queue += start + v(0, 1)
    while (queue.isNotEmpty()) {
        val pos = queue.removeFirst()
        if (pos in visited) continue
        visited += pos
        val (x, y) = pos
        val cell = grid[pos]
        when (cell) {
            '^' -> {
                found += pos
                if (x > 0) {
                    queue += v(x - 1, y)
                }
                if (x < grid[y].lastIndex) {
                    queue += v(x + 1, y)
                }
            }
            '.' -> {
                if (y < grid.lastIndex) {
                    queue += v(x, y + 1)
                }
            }
        }
    }

    return found.size
}

private fun PuzzleInput.part2(): Any {
    val grid = grid
    val start = run {
        var s = v(0, 0)
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] == 'S') {
                    s = v(x, y)
                }
            }
        }
        s
    }

    val cache = mutableMapOf<Vec2i, Long>()
    fun timelines(pos: Vec2i): Long {
        if (pos in cache) return cache[pos]!!

        if (pos.y !in grid.indices || pos.x !in grid[pos.y].indices) {
            return 1L
        }

        val result = when (grid[pos]) {
            '.' -> timelines(pos + v(0, 1))
            '^' -> timelines(pos - v(1, 0)) + timelines(pos + v(1, 0))
            else -> error("Unreachable")
        }

        cache[pos] = result
        return result
    }

    return timelines(start + v(0, 1))
}

fun main() = PuzzleInput(2025, 7).withSolutions({ part1() }, { part2() }).run()