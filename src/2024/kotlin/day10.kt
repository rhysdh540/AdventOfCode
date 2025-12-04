import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val grid = intGridOr(-10)
    val trailheads =
        grid.mapIndexed { y, row -> row.mapIndexed { x, cell -> if (cell == 0) v(x, y) else null } }.flatten()
            .filterNotNull().toMutableList()

    return trailheads.sumOf {
        val visited = mutableSetOf(it)
        val queue = ArrayDeque(listOf(it))
        var count = 0
        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            if (grid[y][x] == 9) {
                count++
            }
            for ((dx, dy) in Vectors.d4) {
                val nx = x + dx
                val ny = y + dy
                val next = grid.getOrNull(ny)?.getOrNull(nx) ?: continue
                if (next - grid[y][x] == 1 && v(nx, ny) !in visited) {
                    visited.add(v(nx, ny))
                    queue.add(v(nx, ny))
                }
            }
        }

        count
    }
}

private fun PuzzleInput.part2(): Any? {
    val grid = intGridOr(-10)
    val trailheads =
        grid.mapIndexed { y, row -> row.mapIndexed { x, cell -> if (cell == 0) v(x, y) else null } }.flatten()
            .filterNotNull().toMutableList()

    return trailheads.sumOf {
        val queue = ArrayDeque(listOf(it))
        var count = 0
        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            if (grid[y][x] == 9) {
                count++
            }
            for ((dx, dy) in Vectors.d4) {
                val nx = x + dx
                val ny = y + dy
                val next = grid.getOrNull(ny)?.getOrNull(nx) ?: continue
                if (next - grid[y][x] == 1) {
                    queue.add(v(nx, ny))
                }
            }
        }

        count
    }
}

fun main() = PuzzleInput(2024, 10).withSolutions({ part1() }, { part2() }).run()