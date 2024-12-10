import Day14.countWeight
import Day14.moveRocksNorth
import Day14.rotateClockwise
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_14(input: String): Any? {
    return countWeight(moveRocksNorth(input.lines().map { it.toList() }))
}

fun part2_14(input: String): Any? {
    var grid = input.lines().map { it.toMutableList() }.toMutableList()
    val cache = mutableMapOf<List<List<Char>>, Int>()

    var cycle = 0
    while(cycle < 1_000_000_000) {
        repeat(4) {
            grid = moveRocksNorth(grid).rotateClockwise()
        }

        if(cache.contains(grid)) {
            val len = cycle - cache[grid]!!
            val remaining = (1_000_000_000 - cycle) % len
            cycle = 1_000_000_000 - remaining
        }

        cache[grid] = cycle++
    }

    return countWeight(grid)
}

object Day14 {
    fun countWeight(grid: List<List<Char>>): Int {
        return grid.indices.sumOf { i ->
            grid[i].count { it == 'O' } * (grid.size - i)
        }
    }

    fun moveRocksNorth(grid: List<List<Char>>): MutableList<MutableList<Char>> {
        val mut = grid.map { it.toMutableList() }.toMutableList()
        for((i, row) in mut.withIndex()) {
            for((j, c) in row.withIndex()) {
                if(c == 'O') {
                    var k = i
                    while (k > 0 && mut[k - 1][j] == '.') {
                        mut[k][j] = '.'
                        mut[k - 1][j] = 'O'
                        k--
                    }
                }
            }
        }

        return mut
    }

    fun List<List<Char>>.rotateClockwise(): MutableList<MutableList<Char>> {
        return MutableList(this[0].size) { i ->
            MutableList(size) { j ->
                this[size - 1 - j][i]
            }
        }
    }
}

fun main() {
    val input = Path("inputs/2023/14.txt").readText()

    var start = System.nanoTime()
    var result = part1_14(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_14(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}