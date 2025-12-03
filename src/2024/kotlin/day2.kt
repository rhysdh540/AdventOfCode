import dev.rdh.aoc.*
import kotlin.math.abs

private fun PuzzleInput.part1(): Any? {
    return lines.map { it.spaced.ints }.count {
        (it == it.sorted() || it == it.sortedDescending()) && it.zipWithNext().all { abs(it.first - it.second) in 1..3 }
    }
}

private fun PuzzleInput.part2(): Any? {
    fun allowed(f: List<Int>) =
        (f == f.sorted() || f == f.sortedDescending()) && f.zipWithNext().all { abs(it.first - it.second) in 1..3 }
    return lines.map { it.spaced.ints }.count { l ->
        allowed(l) || l.indices.any { allowed(l.toMutableList().apply { removeAt(it) }) }
    }
}

fun main() = PuzzleInput(2024, 2).withSolutions({ part1() }, { part2() }).run()