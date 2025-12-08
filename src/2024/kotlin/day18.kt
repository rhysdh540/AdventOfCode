import dev.rdh.aoc.*
import java.util.PriorityQueue

private fun PuzzleInput.part1(): Any? {
    val nums = lines.map { it.split(",").ints.toVec2() }
    val grid = Array(71) { BooleanArray(71) }
    for (i in nums.slice(0..1024)) {
        grid[i.x][i.y] = true
    }

    return solve(grid)
}

private fun PuzzleInput.part2(): Any? {
    val nums = lines.map { it.split(",").ints.toVec2() }
    val grid = Array(71) { BooleanArray(71) }
    for (i in nums.slice(0..1024)) {
        grid[i.x][i.y] = true
    }
    return nums.drop(1024).first {
        grid[it.x][it.y] = true
        solve(grid) == null
    }.let { "${it.x},${it.y}" }
}

private fun solve(grid: Array<BooleanArray>): Int? {
    val pq = PriorityQueue<Pair<Vec2i, Int>>(compareBy { it.second })
    pq.add(Pair(Vec2i.ZERO, 0))
    val visited = Array(71) { BooleanArray(71) }

    while (pq.isNotEmpty()) {
        val (pos, steps) = pq.poll()

        if (pos.x == 70 && pos.y == 70) return steps
        if (visited[pos.y][pos.x]) continue
        visited[pos.y][pos.x] = true

        for (dir in Direction4.entries) {
            val n = pos + dir.vec

            if (n.x in 0..70 && n.y in 0..70 && !grid[n.x][n.y] && !visited[n.y][n.x]) {
                pq.add(Pair(n, steps + 1))
            }
        }
    }

    return null
}

fun main() = PuzzleInput(2024, 18).withSolutions({ part1() }, { part2() }).run()