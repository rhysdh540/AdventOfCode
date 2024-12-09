import Day3.symbols
import Day3.numbers
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_3(input: String): Any? {
    val grid = input.lines().map { it.toCharArray() }.toTypedArray()
    return grid.symbols().sumOf { grid.numbers(it).sum() }
}

fun part2_3(input: String): Any? {
    val grid = input.lines().map { it.toCharArray() }.toTypedArray()
    return grid.symbols().filter { it.isGear && grid.numbers(it).size == 2 }.sumOf {
        grid.numbers(it).reduce { acc, i -> acc * i }
    }
}

object Day3 {
    data class Symbol(val symbol: Char, val x: Int, val y: Int) {
        val isGear = symbol == '*'

        fun getSurrounding(): Array<IntArray> {
            return arrayOf(
                intArrayOf(x - 1, y - 1), intArrayOf(x, y - 1), intArrayOf(x + 1, y - 1),
                intArrayOf(x - 1, y), intArrayOf(x + 1, y),
                intArrayOf(x - 1, y + 1), intArrayOf(x, y + 1), intArrayOf(x + 1, y + 1)
            )
        }
    }

    fun Array<CharArray>.symbols(): List<Symbol> {
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

    fun Array<CharArray>.numbers(sym: Symbol): MutableSet<Int> {
        val numbers = mutableSetOf<Int>()
        for((x, y) in sym.getSurrounding()) {
            var (x, y) = x to y
            if (!this[y][x].isDigit()) continue
            while(x >= 0 && this[y][x] != '.' && this[y][x].isDigit()) {
                x--
            }

            x++
            var start = x
            while(x < this[y].size && this[y][x] != '.' && this[y][x].isDigit()) {
                x++
            }

            numbers.add(this[y].copyOfRange(start, x).joinToString("").toInt())
        }

        return numbers
    }
}

fun main() {
    val input = Path("inputs/2023/3.txt").readText()

    var start = System.nanoTime()
    var result = part1_3(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_3(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}