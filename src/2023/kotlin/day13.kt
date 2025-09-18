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

fun main() {
    val input = getInput(2023, 13)

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