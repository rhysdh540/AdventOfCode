import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

fun part1_8(input: String): Any? {
    val grid = input.lines().map { it.toCharArray() }.toTypedArray()
    val w = grid[0].indices
    val h = grid.indices

    val map = mutableMapOf<Char, List<Pair<Int, Int>>>()
    for (i in h) {
        for (j in w) {
            if (grid[i][j] != '.') {
                map[grid[i][j]] = map.getOrDefault(grid[i][j], listOf()) + Pair(i, j)
            }
        }
    }

    val set = mutableSetOf<Pair<Int, Int>>()
    for ((_, points) in map) {
        for (j in points.indices) {
            for (k in j + 1..points.size - 1) {
                val x = points[j]
                val y = points[k]

                val dx = y.first - x.first
                val dy = y.second - x.second

                set.add(Pair(x.first - dx, x.second - dy))
                set.add(Pair(y.first + dx, y.second + dy))
            }
        }
    }

    return set.filter { it.first in h && it.second in w }.size
}


fun part2_8(input: String): Any? {
    val grid = input.lines().map { it.toCharArray() }.toTypedArray()
    val w = grid[0].indices
    val h = grid.indices

    val map = mutableMapOf<Char, List<Pair<Int, Int>>>()
    for (i in h) {
        for (j in w) {
            if (grid[i][j] != '.') {
                map[grid[i][j]] = map.getOrDefault(grid[i][j], listOf()) + Pair(i, j)
            }
        }
    }

    val set = mutableSetOf<Pair<Int, Int>>()
    for ((_, points) in map) {
        for (j in points.indices) {
            for (k in j + 1..points.size - 1) {
                val x = points[j]
                val y = points[k]

                var dx = y.first - x.first
                var dy = y.second - x.second

                val gcd = gcd(dx, dy)
                dx /= gcd
                dy /= gcd

                // x backwards
                var cx = x.first - dx
                var cy = x.second - dy
                while (cx in h && cy in w) {
                    set.add(Pair(cx, cy))
                    cx -= dx
                    cy -= dy
                }

                // y forwards
                cx = y.first + dx
                cy = y.second + dy
                while (cx in h && cy in w) {
                    set.add(Pair(cx, cy))
                    cx += dx
                    cy += dy
                }

                // between x and y
                cx = x.first
                cy = x.second
                repeat(gcd + 1) {
                    set.add(Pair(cx, cy))
                    cx += dx
                    cy += dy
                }
            }
        }
    }

    return set.size
}

fun gcd(a: Int, b: Int): Int = if (b == 0) abs(a) else gcd(b, a % b)

fun main() {
    val input = Path("inputs/2024/8.txt").readText()

    var start = System.nanoTime()
    var result = part1_8(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_8(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}