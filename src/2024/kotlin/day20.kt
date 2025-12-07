import dev.rdh.aoc.*
import kotlin.math.abs

private fun PuzzleInput.part1(): Any? {
    return run(2)
}

private fun PuzzleInput.part2(): Any? {
    return run(20)
}

private fun PuzzleInput.run(cheatLen: Int): Int {
    fun manhattan(a: Vec2i, b: Vec2i): Int {
        return abs(a.x - b.x) + abs(a.y - b.y)
    }

    lateinit var start: Vec2i
    lateinit var end: Vec2i
    val points = mutableSetOf<Vec2i>()
    lines.forEachIndexed { i, line ->
        line.forEachIndexed { j, c ->
            if (c == 'S') start = v(i, j)
            if (c == 'E') end = v(i, j)
            if (c != '#') points.add(v(i, j))
        }
    }

    val path = run {
        var current = start
        var prev = start

        val result = mutableListOf<Vec2i>()
        while (current != end) {
            val next = Direction4.entries.map { it.vec + current }.first { it != prev && it in points }
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