import kotlin.collections.unzip
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

fun part1_1(input: String): Any? {
    val (first, second) = input.split("\n").map { it.split("   ").let { it[0].toInt() to it[1].toInt() } }.unzip()
    return first.sorted().zip(second.sorted()).sumOf { abs(it.first - it.second) }
}

fun part2_1(input: String): Any? {
    val (first, second) = input.split("\n").map { it.split("   ").let { it[0].toInt() to it[1].toInt() } }.unzip()
    return first.sumOf { i -> i * second.count { it == i } }
}

fun main() {
    val input = Path("inputs/2024/1.txt").readText()

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