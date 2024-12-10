import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_4(input: String): Any? {
    val grid = input.lines().map { it.toCharArray() }.toTypedArray()
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

fun part2_4(input: String): Any? {
    val grid = input.lines().map { it.toCharArray() }.toTypedArray()
    return (1..grid.size - 2).sumOf { r ->
        (1..grid[r].size - 2).count { c ->
            val s = String(charArrayOf(grid[r - 1][c - 1], grid[r - 1][c + 1], grid[r + 1][c - 1], grid[r + 1][c + 1]))
            grid[r][c] == 'A' && (s == "MMSS" || s == "SSMM" || s == "MSMS" || s == "SMSM")
        }
    }
}

fun main() {
    val input = Path("inputs/2024/4.txt").readText()

    var start = System.nanoTime()
    var result = part1_4(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_4(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}