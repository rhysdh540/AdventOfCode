import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

fun part1_20(input: String): Any? {
    return Day20.run(input, 2)
}

fun part2_20(input: String): Any? {
    return Day20.run(input, 20)
}

object Day20 {
    fun run(input: String, cheatLen: Int): Int {
        fun manhattan(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
            return abs(a.first - b.first) + abs(a.second - b.second)
        }

        lateinit var start: Pair<Int, Int>
        lateinit var end: Pair<Int, Int>
        val points = mutableSetOf<Pair<Int, Int>>()
        input.lines().forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                if(c == 'S') start = Pair(i, j)
                if(c == 'E') end = Pair(i, j)
                if(c != '#') points.add(Pair(i, j))
            }
        }

        val path = run {
            var current = start
            var prev = start

            val result = mutableListOf<Pair<Int, Int>>()
            val dirs = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
            while(current != end) {
                val next = dirs.map { Pair(current.first + it.first, current.second + it.second) }
                    .first { it != prev && points.contains(it) }

                result.add(current)
                prev = current
                current = next
            }

            result.add(current)
            return@run result
        }
        val pathLength = path.size

        val distances = path.withIndex().associate { it.value to it.index }

        var count = 0
        for(i in 0 until pathLength) {
            for(j in i + 4 until pathLength) {
                val dist = manhattan(path[i], path[j])
                if(dist <= cheatLen) {
                    val d1 = distances[path[i]]!!
                    val d2 = pathLength - distances[path[j]]!!
                    val newLen = d1 + dist + d2
                    val savings = pathLength - newLen
                    if (savings >= 100) count++
                }
            }
        }

        return count
    }
}

fun main() {
    val input = Path("inputs/2024/20.txt").readText()

    var start = System.nanoTime()
    var result = part1_20(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_20(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}