import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val regex = Regex("""(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)""")
    val instructions = lines.map {
        val (action, x1, y1, x2, y2) = regex.find(it)!!.destructured
        Triple(action, Pair(x1.toInt(), y1.toInt()), Pair(x2.toInt(), y2.toInt()))
    }

    val grid = Array(1000) { BooleanArray(1000) }
    instructions.forEach { (action, p1, p2) ->
        val (x1, y1) = p1
        val (x2, y2) = p2

        for (x in x1..x2) {
            for (y in y1..y2) {
                when (action) {
                    "turn on" -> grid[x][y] = true
                    "turn off" -> grid[x][y] = false
                    "toggle" -> grid[x][y] = !grid[x][y]
                }
            }
        }
    }

    return grid.sumOf { it.cardinality() }
}

private fun PuzzleInput.part2(): Any? {
    val regex = Regex("""(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)""")
    val instructions = lines.map {
        val (action, x1, y1, x2, y2) = regex.find(it)!!.destructured
        Triple(action, Pair(x1.toInt(), y1.toInt()), Pair(x2.toInt(), y2.toInt()))
    }

    val grid = Array(1000) { IntArray(1000) }
    instructions.forEach { (action, p1, p2) ->
        val (x1, y1) = p1
        val (x2, y2) = p2

        for (x in x1..x2) {
            for (y in y1..y2) {
                when (action) {
                    "turn on" -> grid[x][y]++
                    "turn off" -> grid[x][y] = maxOf(0, grid[x][y] - 1)
                    "toggle" -> grid[x][y] += 2
                }
            }
        }
    }

    return grid.sumOf { it.sum() }
}

fun main() = PuzzleInput(2015, 6).withSolutions({ part1() }, { part2() }).run()