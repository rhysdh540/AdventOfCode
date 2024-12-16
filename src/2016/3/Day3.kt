import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_3(input: String): Any? {
    return input.lines()
        .map { Regex("\\d+").findAll(it).map { it.value.toInt() }.toList() }
        .count { (a, b, c) -> a + b > c && a + c > b && b + c > a }
}

fun part2_3(input: String): Int {
    return input.lines()
        .map { Regex("\\d+").findAll(it).map { it.value.toInt() }.toList() }
        .let { rows -> List(3) { col -> rows.map { it[col] } } }
        .flatMap { it.chunked(3) }
        .count { (a, b, c) -> a + b > c && a + c > b && b + c > a }
}

fun main() {
    val input = Path("inputs/2016/3.txt").readText()

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