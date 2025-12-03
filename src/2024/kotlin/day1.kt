import dev.rdh.aoc.*
import kotlin.math.abs

private fun PuzzleInput.part1(): Any? {
    val (first, second) = lines.map { it.split("   ").ints.toPair() }.unzip()
    return first.sorted().zip(second.sorted()).sumOf { abs(it.first - it.second) }
}

private fun PuzzleInput.part2(): Any? {
    val (first, second) = lines.map { it.split("   ").ints.toPair() }.unzip()
    return first.sumOf { i -> i * second.count { it == i } }
}

fun main() = PuzzleInput(2024, 1).withSolutions({ part1() }, { part2() }).run()