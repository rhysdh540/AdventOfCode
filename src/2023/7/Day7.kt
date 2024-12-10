import java.util.Arrays
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_7(input: String): Any? {
    return Day7.run(input, false)
}

fun part2_7(input: String): Any? {
    return Day7.run(input, true)
}

object Day7 {
    fun run(input: String, jokers: Boolean): Int {
        return input.lines().map {
            val parts = it.split(" ")
            Hand(parts[0], getHandType(parts[0], jokers), parts[1].toInt())
        }.sortedWith { a, b ->
            if (a.type != b.type) {
                return@sortedWith a.type.compareTo(b.type)
            }

            val (a, b) = listOf(a, b).map { it.cards.map { getCardValue(it, jokers) }.toIntArray() }
            return@sortedWith Arrays.mismatch(a, b).let {
                b[it] - a[it]
            }
        }.reversed().mapIndexed { i, h ->
            h.bid * (i + 1)
        }.sum()
    }

    fun getCardValue(card: Char, jokers: Boolean): Int {
        return when (card) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> if (jokers) 1 else 11
            'T' -> 10
            else -> card.digitToInt()
        }
    }

    private fun getHandType(hand: String, jokers: Boolean): HandType {
        var hand = hand
        if (hand.contains("J") && jokers) {
            hand = replaceJoker(hand)
        }

        return when (hand.toCharArray().distinct().size) {
            1 -> HandType.FIVE_OF_A_KIND
            2 -> evaluateTwoUnique(hand)
            3 -> evaluateThreeUnique(hand)
            4 -> HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }

    private fun replaceJoker(hand: String): String {
        val frequencyMap = hand.replace("J", "").groupingBy { it }.eachCount().toMutableMap()
        val mostFrequentCard = frequencyMap.maxByOrNull { it.value }?.key
        return hand.replace('J', mostFrequentCard ?: 'A') // if all jokers, replace with A
    }

    private fun evaluateTwoUnique(hand: String): HandType {
        return hand.count { it == hand[0] }.let {
            if (it == 4 || it == 1) HandType.FOUR_OF_A_KIND else HandType.FULL_HOUSE
        }
    }

    private fun evaluateThreeUnique(hand: String): HandType {
        val freq = IntArray(128) {
            hand.count { c -> c.code == it }
        }

        val (countOfOne, countOfTwo, countOfThree) = listOf(1, 2, 3).map { i ->
            freq.count { i == it }
        }

        return when {
            countOfThree == 1 && (countOfTwo == 1 || countOfOne == 2) -> HandType.THREE_OF_A_KIND
            countOfThree == 1 && countOfOne == 1 -> HandType.FULL_HOUSE
            else -> HandType.TWO_PAIR
        }
    }

    data class Hand(val cards: String, val type: HandType, val bid: Int)

    enum class HandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }
}

fun main() {
    val input = Path("inputs/2023/7.txt").readText()

    var start = System.nanoTime()
    var result = part1_7(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_7(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}