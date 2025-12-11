import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return solve().first
}

private fun PuzzleInput.part2(): Any? {
    return solve().second
}

fun PuzzleInput.solve(): Pair<Int, Long> {
    var state = LongArray(grid[0].size)
    state[grid[0].indexOf('S')] = 1
    var splits = 0

    for (y in grid.indices) {
        if (y == 0) continue
        val next = LongArray(state.size)
        for ((x, v) in state.withIndex()) {
            if (v == 0L) continue

            when (grid[x, y]) {
                '.' -> next[x] += v
                '^' -> {
                    splits++
                    if (x > 0) next[x - 1] += v
                    if (x < state.lastIndex) next[x + 1] += v
                }
            }
        }
        state = next
    }

    return splits to state.sum()
}

fun main() = PuzzleInput(2025, 7).withSolutions({ part1() }, { part2() }).run()