import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val grid = intGridOr(-10)
    val trailheads =
        grid.mapIndexed { y, row -> row.mapIndexed { x, cell -> if (cell == 0) Pair(x, y) else null } }.flatten()
            .filterNotNull().toMutableList()
    val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))

    return trailheads.sumOf {
        val visited = mutableSetOf(it)
        val queue = ArrayDeque(listOf(it))
        var count = 0
        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            if (grid[y][x] == 9) {
                count++
            }
            for ((dx, dy) in directions) {
                val nx = x + dx
                val ny = y + dy
                val next = grid.getOrNull(ny)?.getOrNull(nx) ?: continue
                if (next - grid[y][x] == 1 && Pair(nx, ny) !in visited) {
                    visited.add(Pair(nx, ny))
                    queue.add(Pair(nx, ny))
                }
            }
        }

        count
    }
}

private fun PuzzleInput.part2(): Any? {
    val grid = intGridOr(-10)
    val trailheads =
        grid.mapIndexed { y, row -> row.mapIndexed { x, cell -> if (cell == 0) Pair(x, y) else null } }.flatten()
            .filterNotNull().toMutableList()
    val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))

    return trailheads.sumOf {
        val queue = ArrayDeque(listOf(it))
        var count = 0
        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            if (grid[y][x] == 9) {
                count++
            }
            for ((dx, dy) in directions) {
                val nx = x + dx
                val ny = y + dy
                val next = grid.getOrNull(ny)?.getOrNull(nx) ?: continue
                if (next - grid[y][x] == 1) {
                    queue.add(Pair(nx, ny))
                }
            }
        }

        count
    }
}

fun main() {
    val input = getInput(2024, 10)

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