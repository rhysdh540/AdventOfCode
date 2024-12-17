import kotlin.io.path.Path
import kotlin.io.path.readText

import java.util.PriorityQueue

fun part1_16(input: String): Any? {
    return Day16.getPaths(input).first().second
}

fun part2_16(input: String): Any? {
    return Day16.getPaths(input).map { it.first }.flatten().distinct().count()
}

object Day16 {
    fun getPaths(input: String): Sequence<Pair<Set<Pair<Int, Int>>, Int>> {
        val mazeLines = input.lines().map { it.toCharArray() }.toTypedArray()
        val maze = Array(mazeLines.size) { BooleanArray(mazeLines[0].size) }

        var start = Pair(0, 0)
        var end = Pair(0, 0)
        for (i in maze.indices) {
            for (j in maze[i].indices) {
                when (mazeLines[i][j]) {
                    'S' -> start = Pair(i, j)
                    'E' -> end = Pair(i, j)
                    '#' -> maze[i][j] = true
                }
            }
        }

        val directions = listOf(
            Pair(-1, 0),
            Pair(0, 1),
            Pair(1, 0),
            Pair(0, -1)
        )

        data class State(val path: List<Pair<Int, Int>>, val dir: Int, val cost: Int)

        val pq = PriorityQueue<State>(compareBy { it.cost })
        pq.add(State(listOf(start), 1, 0))

        val visited = mutableMapOf<Pair<Pair<Int, Int>, Int>, Int>()
        var minCost: Int = Int.MAX_VALUE

        return generateSequence {
            while (pq.isNotEmpty()) {
                val (path, dir, cost) = pq.poll()
                val c = visited[path.last() to dir]
                if (c != null && c < cost) continue
                visited[path.last() to dir] = cost

                if (path.last() == end) {
                    if (cost <= minCost) minCost = cost
                    else return@generateSequence null
                    return@generateSequence Pair(path.toSet(), cost)
                }

                val (i, j) = path.last()
                val (di, dj) = directions[dir]
                val ni = i + di
                val nj = j + dj

                if (ni in maze.indices && nj in maze[0].indices && !maze[ni][nj]) {
                    pq.add(State(path + Pair(ni, nj), dir, cost + 1))
                }

                val leftDir = (dir + 3) % 4
                val rightDir = (dir + 1) % 4
                pq.add(State(path, leftDir, cost + 1000))
                pq.add(State(path, rightDir, cost + 1000))
            }
            error("No path found")
        }
    }
}

fun main() {
    val input = Path("inputs/2024/16.txt").readText()

    var start = System.nanoTime()
    var result = part1_16(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_16(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}