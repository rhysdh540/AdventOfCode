import dev.rdh.aoc.*
import kotlin.math.abs

private fun PuzzleInput.part1(): Any? {
    val (first, second) = lines.map { it.split("   ").let { it[0].toInt() to it[1].toInt() } }.unzip()
    return first.sorted().zip(second.sorted()).sumOf { abs(it.first - it.second) }
}

private fun PuzzleInput.part2(): Any? {
    val (first, second) = lines.map { it.split("   ").let { it[0].toInt() to it[1].toInt() } }.unzip()
    return first.sumOf { i -> i * second.count { it == i } }
}

fun main() {
    val input = PuzzleInput(2024, 1)

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