import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.min
import kotlin.math.pow

fun part1_4(input: String): Any? {
    return input.lines().sumOf { 2.0.pow(Day4.getWinningNumbers(it) - 1).toInt() }
}

fun part2_4(input: String): Any? {
    val winningNums = input.lines().map { Day4.getWinningNumbers(it) }
    return winningNums.foldIndexed(IntArray(winningNums.size) { 1 }) { i, counts, num ->
        for (j in i + 1 until min(i + num + 1, winningNums.size)) {
            counts[j] += counts[i]
        }
        counts
    }.sum()
}

object Day4 {
    fun getWinningNumbers(line: String): Int {
        val (_, nums, wins) = line.split(':', '|')
        return nums.trim().split(Regex(" +"))
            .intersect(wins.trim().split(Regex(" +")))
            .size
    }
}

fun main() {
    val input = Path("inputs/2023/4.txt").readText()

    var start = System.nanoTime()
    var result = part1_4(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_4(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}