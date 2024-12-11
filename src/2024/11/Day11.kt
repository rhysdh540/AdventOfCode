import kotlin.io.path.Path
import kotlin.io.path.readText

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
            if (key in memo) return memo[key]!!

            val transformed = when {
                stone == 0L -> listOf<Long>(1)
                stone.toString().length % 2 == 0 -> {
                    val s = stone.toString()
                    val half = s.length / 2
                    val left = s.substring(0, half).toLong()
                    val right = s.substring(half).toLong()
                    listOf(left, right)
                }

                else -> listOf(stone * 2024)
            }

            var total = 0L
            for (newStone in transformed) {
                total += count(newStone, n - 1)
            }

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