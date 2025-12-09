import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val startPos = charGrid.indexOfFirst { 'S' in it }.let { v(charGrid[it].indexOf('S'), it) }
    val grid = boolGrid('#')

    var current = setOf(startPos)
    repeat(64) {
        current = current.flatMap { pos ->
            Direction4.entries.map { pos + it.vec }.filter { (x, y) ->
                x in grid[0].indices && y in grid.indices && !grid[y][x]
            }
        }.toSet()
    }

    return current.size
}

private fun PuzzleInput.part2(): Any {
    return "not implemented yet"
}

fun main() = PuzzleInput(2023, 21).withSolutions({ part1() }, { part2() }).run()