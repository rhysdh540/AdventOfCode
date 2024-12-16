import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

fun part1_1(input: String): Any? {
    val insns = input.split(", ").map { Pair(it[0], it.drop(1).toInt()) }
    var (x, y) = Pair(0, 0)
    var dir = 0

    for ((turn, dist) in insns) {
        dir = (dir + if (turn == 'R') 1 else 3) % 4
        when (dir) {
            0 -> y += dist
            1 -> x += dist
            2 -> y -= dist
            3 -> x -= dist
        }
    }

    return abs(x) + abs(y)
}

fun part2_1(input: String): Any? {
    val insns = input.split(", ").map { Pair(it[0], it.drop(1).toInt()) }
    var (x, y) = Pair(0, 0)
    var dir = 0
    val visited = mutableSetOf<Pair<Int, Int>>()

    for ((turn, dist) in insns) {
        dir = (dir + if (turn == 'R') 1 else 3) % 4
        repeat(dist) {
            when (dir) {
                0 -> y++
                1 -> x++
                2 -> y--
                3 -> x--
            }
            if (!visited.add(Pair(x, y))) {
                return abs(x) + abs(y)
            }
        }
    }

    error("No location visited twice")
}

fun main() {
    val input = Path("inputs/2016/1.txt").readText()

    var start = System.nanoTime()
    var result = part1_1(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_1(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}