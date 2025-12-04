import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val dirs = listOf(
        v(0, 1), v(1, 0), v(1, 1), v(1, -1),
        v(0, -1), v(-1, 0), v(-1, -1), v(-1, 1)
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

fun main() = PuzzleInput(2024, 4).withSolutions({ part1() }, { part2() }).run()