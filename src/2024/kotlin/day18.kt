import dev.rdh.aoc.*
import java.util.PriorityQueue

private fun PuzzleInput.part1(): Any? {
    val nums = lines.map { it.split(",").ints.toPair() }
    val grid = Array(71) { BooleanArray(71) }
    for (i in nums.slice(0..1024)) {
        grid[i.x][i.y] = true
    }

    return solve(grid)
}

private fun PuzzleInput.part2(): Any? {
    val nums = lines.map { it.split(",").ints.toPair() }
    val grid = Array(71) { BooleanArray(71) }
    return nums.drop(1024).first {
        grid[it.x][it.y] = true
        solve(grid) == null
    }.let { "${it.x},${it.y}" }
}

private fun solve(grid: Array<BooleanArray>): Int? {
    val pq = PriorityQueue<Triple<Int, Int, Int>>(compareBy { it.third })
    pq.add(Triple(0, 0, 0))
    val visited = Array(71) { BooleanArray(71) }

    while (pq.isNotEmpty()) {
        val (x, y, steps) = pq.poll()

        if (x == 70 && y == 70) return steps
        if (visited[x][y]) continue
        visited[x][y] = true

        for ((dx, dy) in Vectors.d4) {
            val nx = x + dx
            val ny = y + dy

            if (nx in 0..70 && ny in 0..70 && !grid[nx][ny] && !visited[nx][ny]) {
                pq.add(Triple(nx, ny, steps + 1))
            }
        }
    }

    return null
}

fun main() = PuzzleInput(2024, 18).withSolutions({ part1() }, { part2() }).run()