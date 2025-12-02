import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val startPos = charGrid.indexOfFirst { 'S' in it }.let { charGrid[it].indexOf('S') to it }
    val grid = boolGrid

    var current = setOf(startPos)
    repeat(64) {
        current = current.flatMap { (x, y) ->
            listOf(x - 1 to y, x + 1 to y, x to y - 1, x to y + 1).filter { (xx, yy) ->
                xx in grid[0].indices && yy in grid.indices && !grid[yy][xx]
            }
        }.toSet()
    }

    return current.size
}

private fun PuzzleInput.part2(): Any {
    return "not implemented yet"
}

fun main() {
    val input = PuzzleInput(2023, 21)

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