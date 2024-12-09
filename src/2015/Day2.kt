import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_2(input: String): Any? {
    return input.lines().sumOf {
        val (l, w, h) = it.split("x").map { it.toInt() }
        (2 * l * w) + (2 * l * h) + (2 * w * h) + minOf(l * w, l * h, w * h)
    }
}

fun part2_2(input: String): Any? {
    return input.lines().sumOf {
        val (l, w, h) = it.split("x").map { it.toInt() }
        2 * minOf(l + w, l + h, w + h) + (l * w * h)
    }
}

fun main() {
    val input = Path("inputs/2015/2.txt").readText()

    var start = System.nanoTime()
    var result = part1_2(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_2(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}