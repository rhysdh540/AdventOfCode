import dev.rdh.aoc.*
import kotlin.math.min
import kotlin.math.pow

private fun PuzzleInput.part1(): Any? {
    return lines.sumOf { 2.0.pow(getWinningNumbers(it) - 1).toInt() }
}

private fun PuzzleInput.part2(): Any? {
    val winningNums = lines.map { getWinningNumbers(it) }
    return winningNums.foldIndexed(IntArray(winningNums.size) { 1 }) { i, counts, num ->
        for (j in i + 1 until min(i + num + 1, winningNums.size)) {
            counts[j] += counts[i]
        }
        counts
    }.sum()
}

private fun getWinningNumbers(line: String): Int {
    val (_, nums, wins) = line.split(':', '|')
    return nums.trim().split(Regex(" +"))
        .intersect(wins.trim().split(Regex(" +")))
        .size
}

fun main() {
    val input = getInput(2023, 4)

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