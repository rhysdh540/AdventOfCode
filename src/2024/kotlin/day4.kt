import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val dirs = listOf(
        Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(1, -1),
        Pair(0, -1), Pair(-1, 0), Pair(-1, -1), Pair(-1, 1)
    )

    return grid.indices.sumOf { r ->
        grid[r].indices.sumOf { c ->
            dirs.count { (dr, dc) ->
                "XMAS".indices.all { i ->
                    val nr = r + i * dr
                    val nc = c + i * dc
                    nr in grid.indices && nc in grid[r].indices && grid[nr][nc] == "XMAS"[i]
                }
            }
        }
    }
}

private fun PuzzleInput.part2(): Any? {
    return (1..grid.size - 2).sumOf { r ->
        (1..grid[r].size - 2).count { c ->
            val s = String(charArrayOf(grid[r - 1][c - 1], grid[r - 1][c + 1], grid[r + 1][c - 1], grid[r + 1][c + 1]))
            grid[r][c] == 'A' && s in setOf("MMSS", "SSMM", "MSMS", "SMSM")
        }
    }
}

fun main() {
    val input = PuzzleInput(2024, 4)

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