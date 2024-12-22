import java.util.BitSet
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_22(input: String): Any? {
    return input.lines().sumOf { Day22.seq(it.toLong()).take(2001).last() }
}

fun part2_22(input: String): Any? {
    val allPrices = mutableMapOf<Int, Long>()

    fun key(diffs: List<Int>): Int {
        return diffs.fold(0) { acc, diff -> (acc shl 4) or (diff and 0xF) }
    }

    input.lines().map { it.toLong() }.forEach { num ->
        val prices = Day22.seq(num).take(2001).map { it % 10 }.toList()
        val diffs = prices.zipWithNext { a, b -> (b - a).toInt() }.windowed(4)
        val seen = BitSet()
        prices.drop(4).zip(diffs).forEach { (price, diff) ->
            val key = key(diff)
            if(!seen[key]) {
                seen.set(key)
                allPrices.merge(key, price, Long::plus)
            }
        }
    }

    return allPrices.maxOf { it.value }
}

object Day22 {
    fun seq(num: Long) = generateSequence(num) {
        var n = it xor ((it shl 6) and 0xFFFFFFL)
        n = n xor ((n ushr 5) and 0xFFFFFFL)
        n xor ((n shl 11) and 0xFFFFFFL)
    }
}

fun main() {
    val input = Path("inputs/2024/22.txt").readText()

    var start = System.nanoTime()
    var result = part1_22(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_22(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}