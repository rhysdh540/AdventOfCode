import kotlin.io.path.Path
import kotlin.io.path.readText

import java.util.PriorityQueue

fun part1_16(input: String): Any? {
    val mazeS = input.lines().map { it.toCharArray() }.toTypedArray()
    val maze = Array(mazeS.size) { i -> Array(mazeS[0].size) { j -> mazeS[i][j] == '#' } }

    var start = Pair(0, 0)
    var end = Pair(0, 0)
    for (i in maze.indices) {
        for (j in maze[i].indices) {
            when (mazeS[i][j]) {
                'S' -> start = Pair(i, j)
                'E' -> end = Pair(i, j)
            }
        }
    }

    val directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1)) // N, E, S, W
    data class State(val i: Int, val j: Int, val dir: Int, val cost: Int)
    val pq = PriorityQueue<State>(compareBy { it.cost })
    val visited = mutableSetOf<Triple<Int, Int, Int>>()

    pq.add(State(start.first, start.second, 1, 0))

    while (pq.isNotEmpty()) {
        val current = pq.poll()
        if (Triple(current.i, current.j, current.dir) in visited) continue
        visited.add(Triple(current.i, current.j, current.dir))

        if (current.i == end.first && current.j == end.second) return current.cost

        val (di, dj) = directions[current.dir]
        val ni = current.i + di
        val nj = current.j + dj
        if (ni in maze.indices && nj in maze[0].indices && !maze[ni][nj]) {
            pq.add(State(ni, nj, current.dir, current.cost + 1))
        }

        val leftDir = (current.dir + 3) % 4
        val rightDir = (current.dir + 1) % 4
        pq.add(State(current.i, current.j, leftDir, current.cost + 1000))
        pq.add(State(current.i, current.j, rightDir, current.cost + 1000))
    }

    error("No path found")
}

fun part2_16(input: String): Any? {
    val mazeS = input.lines().map { it.toCharArray() }.toTypedArray()
    val maze = Array(mazeS.size) { i -> Array(mazeS[0].size) { j -> mazeS[i][j] == '#' } }

    var start = Pair(0, 0)
    var end = Pair(0, 0)
    for (i in maze.indices) {
        for (j in maze[i].indices) {
            when (mazeS[i][j]) {
                'S' -> start = Pair(i, j)
                'E' -> end = Pair(i, j)
            }
        }
    }

    val directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))
    data class State(val path: List<Pair<Int, Int>>, val dir: Int, val cost: Int)
    val pq = PriorityQueue<State>(compareBy { it.cost })
    val visited = mutableMapOf<Pair<Pair<Int, Int>, Int>, Int>()

    var min = Int.MAX_VALUE
    val allPaths = mutableSetOf<List<Pair<Int, Int>>>()

    pq.add(State(listOf(start), 1, 0))

    while (pq.isNotEmpty()) {
        val (path, dir, cost) = pq.poll()
        if (visited[path.last() to dir] != null && visited[path.last() to dir]!! < cost) continue
        visited[path.last() to dir] = cost

        if (path.last() == end) {
            if (cost <= min) min = cost
            else return allPaths.flatten().distinct().size
            allPaths.add(path)
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