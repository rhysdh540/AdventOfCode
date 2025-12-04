import dev.rdh.aoc.*
import kotlin.math.abs

private fun PuzzleInput.part1(): Any? {
    return run(2)
}

private fun PuzzleInput.part2(): Any? {
    return run(20)
}

private fun PuzzleInput.run(cheatLen: Int): Int {
    fun manhattan(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
        return abs(a.x - b.x) + abs(a.y - b.y)
    }

    lateinit var start: Pair<Int, Int>
    lateinit var end: Pair<Int, Int>
    val points = mutableSetOf<Pair<Int, Int>>()
    lines.forEachIndexed { i, line ->
        line.forEachIndexed { j, c ->
            if (c == 'S') start = Pair(i, j)
            if (c == 'E') end = Pair(i, j)
            if (c != '#') points.add(Pair(i, j))
        }
    }

    val path = run {
        var current = start
        var prev = start

        val result = mutableListOf<Pair<Int, Int>>()
        while (current != end) {
            val next = Vectors.d4.map { it + current }.first { it != prev && it in points }
            result.add(current)
            prev = current
            current = next
        }

        result.add(current)
        return@run result
    }
    val pathLength = path.size

    val distances = path.withIndex().associate { it.value to it.index }

    var count = 0
    for (i in 0 until pathLength) {
        for (j in i + 4 until pathLength) {
            val dist = manhattan(path[i], path[j])
            if (dist <= cheatLen) {
                val d1 = distances[path[i]]!!
                val d2 = pathLength - distances[path[j]]!!
                val newLen = d1 + dist + d2
                val savings = pathLength - newLen
                if (savings >= 100) count++
            }
        }
    }

    return count
}

fun main() = PuzzleInput(2024, 20).withSolutions({ part1() }, { part2() }).run()