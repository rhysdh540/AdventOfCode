import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val locks = sections.filter {
        it.lines().first().all { it == '#' }
    }.map {
        val skip = it.substringAfter("\n")
        skip.rotateRight().lines().map { it.count { it == '#' } }
    }

    val keys = sections.filter {
        it.lines().first().all { it == '.' }
    }.map {
        val skip = it.substringBeforeLast("\n")
        skip.rotateRight().lines().map { it.count { it == '#' } }
    }

    val locksToKeys = locks.flatMap { l ->
        keys.map { k -> l to k }
    }

    return locksToKeys.count { (l, k) ->
        l.zip(k).all { it.first + it.second <= 5 }
    }
}

private fun PuzzleInput.part2(): Any? {
    return "Merry Christmas!"
}

private fun String.rotateRight(): String {
    val lines = this.lines()
    return (0 until lines.first().length).joinToString("\n") { i ->
        lines.reversed().map { it[i] }.joinToString("")
    }
}

fun main() = PuzzleInput(2024, 25).withSolutions({ part1() }, { part2() }).run()