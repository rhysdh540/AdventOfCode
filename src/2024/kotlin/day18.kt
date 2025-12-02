import dev.rdh.aoc.*
import java.util.PriorityQueue

private fun PuzzleInput.part1(): Any? {
    val nums = input.lines().map { it.split(",").let { it[0].toInt() to it[1].toInt() } }
    val grid = Array(71) { BooleanArray(71) }
    for (i in nums.slice(0..1024)) {
        grid[i.first][i.second] = true
    }

    return solve(grid)
}

private fun PuzzleInput.part2(): Any? {
    val nums = input.lines().map { it.split(",").let { it[0].toInt() to it[1].toInt() } }
    val grid = Array(71) { BooleanArray(71) }
    return nums.drop(1024).first {
        grid[it.first][it.second] = true
        solve(grid) == null
    }.let { "${it.first},${it.second}" }
}

private fun solve(grid: Array<BooleanArray>): Int? {
    val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))
    val pq = PriorityQueue<Triple<Int, Int, Int>>(compareBy { it.third })
    pq.add(Triple(0, 0, 0))
    val visited = Array(71) { BooleanArray(71) }

    while (pq.isNotEmpty()) {
        val (x, y, steps) = pq.poll()

        if (x == 70 && y == 70) return steps
        if (visited[x][y]) continue
        visited[x][y] = true

        for ((dx, dy) in directions) {
            val nx = x + dx
            val ny = y + dy

            if (nx in 0..70 && ny in 0..70 && !grid[nx][ny] && !visited[nx][ny]) {
                pq.add(Triple(nx, ny, steps + 1))
            }
        }
    }

    return null
}

fun main() {
    val input = PuzzleInput(2024, 18)

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