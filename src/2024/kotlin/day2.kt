import dev.rdh.aoc.*
import kotlin.math.abs

private fun PuzzleInput.part1(): Any? {
    return lines.map { it.split(' ').toInts() }.count {
        (it == it.sorted() || it == it.sortedDescending()) && it.zipWithNext().all { abs(it.first - it.second) in 1..3 }
    }
}

private fun PuzzleInput.part2(): Any? {
    fun allowed(f: List<Int>) = (f == f.sorted() || f == f.sortedDescending()) && f.zipWithNext().all { abs(it.first - it.second) in 1..3 }
    return lines.map { it.split(' ').toInts() }.count { l ->
        allowed(l) || l.indices.any { allowed(l.toMutableList().apply { removeAt(it) }) }
    }
}

fun main() {
    val input = getInput(2024, 2)

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