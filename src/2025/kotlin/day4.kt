import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return reachable(boolGrid('@')).size
}

private fun PuzzleInput.part2(): Any? {
    val grid = boolGrid('@').deepToMutableList()
    var removed = 0
    while (true) {
        val toRemove = reachable(grid)
        if (toRemove.isEmpty()) break
        for (pos in toRemove) {
            grid[pos] = false
        }
        removed += toRemove.size
    }
    return removed
}

private fun reachable(grid: List<List<Boolean>>): Set<Pair<Int, Int>> {
    val reachable = mutableSetOf<Pair<Int, Int>>()
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            if (!grid[x, y]) continue
            val around = Vectors.d8.map { it + v(x, y) }

            val nrolls = around.count { (ax, ay) ->
                ay in grid.indices && ax in grid[ay].indices && grid[ax, ay]
            }

            if (nrolls < 4) {
                reachable += v(x, y)
            }
        }
    }
    return reachable
}

fun main() = PuzzleInput(2025, 4).withSolutions({ part1() }, { part2() }).run()