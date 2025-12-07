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

private fun reachable(grid: List2d<Boolean>): Set<Vec2i> {
    val reachable = mutableSetOf<Vec2i>()
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            if (!grid[x, y]) continue
            val around = Direction8.entries.map { v(x, y) + it.vec }

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