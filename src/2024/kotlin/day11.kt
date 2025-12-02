import dev.rdh.aoc.*
import kotlin.math.log10
import kotlin.math.pow

private fun PuzzleInput.part1(): Any? {
    return run(25)
}

private fun PuzzleInput.part2(): Any? {
    return run(75)
}

private fun PuzzleInput.run(times: Int): Long {
    val stones = input.spaced.longs
    val memo = mutableMapOf<Pair<Long, Int>, Long>()

    fun count(stone: Long, n: Int): Long {
        if (n == 0) return 1

        val key = stone to n
        memo[key]?.let { return it }

        val transformed = if (stone == 0L) listOf(1L) else {
            val numDigits = log10(stone.toDouble()).toInt() + 1
            if (numDigits and 1 == 0) {
                val divisor = 10.0.pow(numDigits / 2).toLong()
                listOf(stone / divisor, stone % divisor)
            } else {
                listOf(stone * 2024)
            }
        }

        val total = transformed.sumOf { count(it, n - 1) }
        memo[key] = total
        return total
    }

    return stones.sumOf { count(it, times) }
}

fun main() {
    val input = PuzzleInput(2024, 11)

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