import dev.rdh.aoc.*
import kotlin.collections.sumOf

private fun PuzzleInput.part1(): Any? {
    val ranges = getMergedRanges()
    return sections[1].lines().longs.count { i ->
        ranges.any { i in it }
    }
}

private fun PuzzleInput.part2(): Any? {
    return getMergedRanges().sumOf { it.last - it.first + 1 }
}

private fun PuzzleInput.getMergedRanges(): List<LongRange> {
    val ranges = sections[0].lines().map {
        val p = it.split('-').longs
        p[0]..p[1]
    }

    val merged = mutableListOf<LongRange>()
    for (range in ranges.sortedBy { it.first }) {
        if (merged.isEmpty() || merged.last().last < range.first) {
            merged.add(range)
        } else {
            val last = merged.last()
            merged[merged.size - 1] = last.first..maxOf(last.last, range.last)
        }
    }
    return merged
}

fun main() = PuzzleInput(2025, 5).withSolutions({ part1() }, { part2() }).run()