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

fun main() = PuzzleInput(2023, 4).withSolutions({ part1() }, { part2() }).run()