import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_6(input: String): Any? {
    val regex = Regex("""(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)""")
    val instructions = input.lines().map {
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

    return grid.sumOf { it.count { it } }
}

fun part2_6(input: String): Any? {
    val regex = Regex("""(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)""")
    val instructions = input.lines().map {
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

fun main() {
    val input = Path("inputs/2015/6.txt").readText()

    var start = System.nanoTime()
    var result = part1_6(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_6(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}