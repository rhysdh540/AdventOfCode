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
            val pos = queue.removeFirst()
            if (grid[pos] == 9) {
                count++
            }
            for (dir in Direction4.entries) {
                val n = pos + dir.vec
                val next = grid.getOrNull(n.y)?.getOrNull(n.x) ?: continue
                if (next - grid[pos] == 1 && n !in visited) {
                    visited.add(n)
                    queue.add(n)
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
            val pos = queue.removeFirst()
            if (grid[pos] == 9) {
                count++
            }
            for (dir in Direction4.entries) {
                val n = pos + dir.vec
                val next = grid.getOrNull(n.y)?.getOrNull(n.x) ?: continue
                if (next - grid[pos] == 1) {
                    queue.add(n)
                }
            }
        }

        count
    }
}

fun main() = PuzzleInput(2024, 10).withSolutions({ part1() }, { part2() }).run()