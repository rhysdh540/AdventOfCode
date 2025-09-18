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
    if(differences.all { it == 0 }) return Pair(0, 0)

    val nextPair = calculateDifferences(differences)
    val prev = differences.first() - nextPair.first
    val next = differences.last() + nextPair.second
    return Pair(prev, next)
}

fun main() {
    val input = getInput(2023, 9)

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