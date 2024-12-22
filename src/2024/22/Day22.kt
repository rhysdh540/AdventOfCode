import java.util.BitSet
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_22(input: String): Any? {
    return input.lines().sumOf { Day22.seq(it.toLong()).take(2001).last() }
}

fun part2_22(input: String): Any? {
    val allPrices = mutableMapOf<Int, Long>()

    // hashes the last 4 price diffs from index (i + 5)
    fun key(prices: List<Long>, i: Int): Int {
        val a = prices[i].toInt()
        val b = prices[i + 1].toInt()
        val c = prices[i + 2].toInt()
        val d = prices[i + 3].toInt()
        val e = prices[i + 4].toInt()

        var r = (a - b) and 0xF
        r = (r shl 4) or ((b - c) and 0xF)
        r = (r shl 4) or ((c - d) and 0xF)
        r = (r shl 4) or ((d - e) and 0xF)
        return r
    }

    input.lines().forEach { num ->
        val prices = Day22.seq(num.toLong()).take(2001).map { it % 10 }.toList()
        val seen = BitSet()
        prices.drop(4).forEachIndexed { i, price ->
            val key = key(prices, i)
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