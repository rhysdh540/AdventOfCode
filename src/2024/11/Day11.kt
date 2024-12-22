import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.log10
import kotlin.math.pow

fun part1_11(input: String): Any? {
    return Day11.run(input, 25)
}

fun part2_11(input: String): Any? {
    return Day11.run(input, 75)
}

object Day11 {
    fun run(input: String, times: Int): Long {
        val stones = input.split(' ').map { it.toLong() }
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
}

fun main() {
    val input = Path("inputs/2024/11.txt").readText()

    var start = System.nanoTime()
    var result = part1_11(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_11(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}