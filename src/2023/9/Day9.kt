import Day9.calculateDifferences
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_9(input: String): Any? {
    return input.lines().map { calculateDifferences(it) }.sumOf { it.second }
}

fun part2_9(input: String): Any? {
    return input.lines().map { calculateDifferences(it) }.sumOf { it.first }
}

object Day9 {
    fun calculateDifferences(line: String): Pair<Int, Int> {
        val nums = line.split(" ").map { it.toInt() }
        val diff = calculateDifferences(nums)
        return Pair(nums.first() - diff.first, nums.last() + diff.second)
    }

    private fun calculateDifferences(nums: List<Int>): Pair<Int, Int> {
        val differences = nums.zipWithNext { a, b -> b - a }
        if(differences.all { it == 0 }) return Pair(0, 0)

        val nextPair = calculateDifferences(differences)
        val prev = differences.first() - nextPair.first
        val next = differences.last() + nextPair.second
        return Pair(prev, next)
    }
}

fun main() {
    val input = Path("inputs/2023/9.txt").readText()

    var start = System.nanoTime()
    var result = part1_9(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_9(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}