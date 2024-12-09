import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_1(input: String): Any? {
    return input.lines().sumOf {
        (it.first { it.isDigit() }.digitToInt() * 10) + it.last { it.isDigit() }.digitToInt()
    }
}

fun part2_1(input: String): Any? {
    val digits = mapOf(
        "one" to "o1e",
        "two" to "t2o",
        "three" to "t3e",
        "four" to "f4r",
        "five" to "f5e",
        "six" to "s6x",
        "seven" to "s7n",
        "eight" to "e8t",
        "nine" to "n9e",
        "zero" to "z0o"
    )
    var fixed = input
    digits.forEach { (key, value) ->
        fixed = fixed.replace(key, value)
    }

    return part1_1(fixed)
}

fun main() {
    val input = Path("inputs/2023/1.txt").readText()

    var start = System.nanoTime()
    var result = part1_1(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_1(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}