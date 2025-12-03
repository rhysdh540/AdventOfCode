import dev.rdh.aoc.*
import java.util.BitSet

private fun PuzzleInput.part1(): Any? {
    return lines.sumOf { seq(it.toLong()).take(2001).last() }
}

private fun PuzzleInput.part2(): Any? {
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

    lines.forEach { num ->
        val prices = seq(num.toLong()).take(2001).map { it % 10 }.toList()
        val seen = BitSet()
        prices.drop(4).forEachIndexed { i, price ->
            val key = key(prices, i)
            if (!seen[key]) {
                seen.set(key)
                allPrices.merge(key, price, Long::plus)
            }
        }
    }

    return allPrices.maxOf { it.value }
}

private fun seq(num: Long) = generateSequence(num) {
    var n = it xor ((it shl 6) and 0xFFFFFFL)
    n = n xor ((n ushr 5) and 0xFFFFFFL)
    n xor ((n shl 11) and 0xFFFFFFL)
}

fun main() = PuzzleInput(2024, 22).withSolutions({ part1() }, { part2() }).run()