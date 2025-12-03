import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return lines.sumOf { calculateDifferences(it).second }
}

private fun PuzzleInput.part2(): Any? {
    return lines.sumOf { calculateDifferences(it).first }
}

private fun calculateDifferences(line: String): Pair<Int, Int> {
    val nums = line.spaced.ints
    val diff = calculateDifferences(nums)
    return Pair(nums.first() - diff.first, nums.last() + diff.second)
}

private fun calculateDifferences(nums: List<Int>): Pair<Int, Int> {
    val differences = nums.zipWithNext { a, b -> b - a }
    if (differences.all { it == 0 }) return Pair(0, 0)

    val nextPair = calculateDifferences(differences)
    val prev = differences.first() - nextPair.first
    val next = differences.last() + nextPair.second
    return Pair(prev, next)
}

fun main() = PuzzleInput(2023, 9).withSolutions({ part1() }, { part2() }).run()