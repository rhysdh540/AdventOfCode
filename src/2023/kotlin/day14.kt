import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return countWeight(moveRocksNorth(grid))
}

private fun PuzzleInput.part2(): Any? {
    var grid = grid
    val cache = mutableMapOf<List<List<Char>>, Int>()

    var cycle = 0
    while(cycle < 1_000_000_000) {
        repeat(4) {
            grid = moveRocksNorth(grid).rotateClockwise()
        }

        if(grid in cache) {
            val len = cycle - cache[grid]!!
            val remaining = (1_000_000_000 - cycle) % len
            cycle = 1_000_000_000 - remaining
        }

        cache[grid] = cycle++
    }

    return countWeight(grid)
}

private fun countWeight(grid: List<List<Char>>): Int {
    return grid.indices.sumOf { i ->
        grid[i].count { it == 'O' } * (grid.size - i)
    }
}

private fun moveRocksNorth(grid: List<List<Char>>): MutableList<MutableList<Char>> {
    val mut = grid.deepToMutableList()
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

fun main() {
    val input = getInput(2023, 14)

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