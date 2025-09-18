import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return grid.symbols().sumOf { grid.numbers(it).sum() }
}

private fun PuzzleInput.part2(): Any? {
    return grid.symbols().filter { it.isGear && grid.numbers(it).size == 2 }.sumOf {
        grid.numbers(it).reduce { acc, i -> acc * i }
    }
}

private data class Symbol(val symbol: Char, val x: Int, val y: Int) {
    val isGear = symbol == '*'

    fun getSurrounding(): Array<IntArray> {
        return arrayOf(
            intArrayOf(x - 1, y - 1), intArrayOf(x, y - 1), intArrayOf(x + 1, y - 1),
            intArrayOf(x - 1, y),                           intArrayOf(x + 1, y),
            intArrayOf(x - 1, y + 1), intArrayOf(x, y + 1), intArrayOf(x + 1, y + 1)
        )
    }
}

private fun List<List<Char>>.symbols(): List<Symbol> {
    val symbols = mutableListOf<Symbol>()
    for (y in indices) {
        for (x in this[y].indices) {
            if(this[y][x] in "@#$%^&*\\-=+/") {
                symbols.add(Symbol(this[y][x], x, y))
            }
        }
    }

    return symbols
}

private fun List<List<Char>>.numbers(sym: Symbol): MutableSet<Int> {
    val numbers = mutableSetOf<Int>()
    for((x, y) in sym.getSurrounding()) {
        var (x, y) = x to y
        if (!this[y][x].isDigit()) continue
        while(x >= 0 && this[y][x] != '.' && this[y][x].isDigit()) {
            x--
        }

        x++
        val start = x
        while(x < this[y].size && this[y][x] != '.' && this[y][x].isDigit()) {
            x++
        }

        numbers.add(this[y].subList(start, x).joinToString("").toInt())
    }

    return numbers
}

fun main() {
    val input = getInput(2023, 3)

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