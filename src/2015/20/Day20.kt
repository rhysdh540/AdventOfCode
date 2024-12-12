import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_20(input: String): Any? {
    val input = input.toInt()
    val houses = IntArray(input / 10)
    for (i in 1 until houses.size) {
        for (j in i until houses.size step i) {
            houses[j] += i * 10
        }
    }

    return houses.indexOfFirst { it >= input }
}

fun part2_20(input: String): Any? {
    val input = input.toInt()
    val houses = IntArray(input / 10)
    for (i in 1 until houses.size) {
        for (j in i until minOf(i * 50 + 1, houses.size) step i) {
            houses[j] += i * 11
        }
    }

    return houses.indexOfFirst { it >= input }
}

fun main() {
    val input = Path("inputs/2015/20.txt").readText()

    var start = System.nanoTime()
    var result = part1_20(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_20(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}