import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return run(false)
}

private fun PuzzleInput.part2(): Any? {
    return run(true)
}

private fun PuzzleInput.run(fixSmudge: Boolean): Int {
    val grids = sections.map { it.lines() }
    return grids.sumOf {
        val found = findReflection(it, fixSmudge)
        if (found != 0) return@sumOf found * 100
        else findReflection(it.rotateClockwise(), fixSmudge)
    }
}

private fun findReflection(grid: List<String>, fixSmudge: Boolean): Int {
    for (r in 1..<grid.size) {
        val diff = grid.slice(0 until r).reversed().zip(
            grid.slice(r until grid.size)
        ).sumOf { (rowA, rowB) ->
            rowA.zip(rowB).count { (a, b) -> a != b }
        }

        if (diff == if (fixSmudge) 1 else 0) {
            return r
        }
    }

    return 0
}

fun main() = PuzzleInput(2023, 13).withSolutions({ part1() }, { part2() }).run()