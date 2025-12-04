import dev.rdh.aoc.*
import dev.rdh.aoc.permutations

private fun PuzzleInput.part1(): Any? {
    return run(Iterable<Iterable<String>>::minOf)
}

private fun PuzzleInput.part2(): Any? {
    return run(Iterable<Iterable<String>>::maxOf)
}

private inline fun PuzzleInput.run(finder: Iterable<Iterable<String>>.((Iterable<String>) -> Int) -> Int): Int {
    val distances = mutableMapOf<Pair<String, String>, Int>()
    lines.forEach {
        val (from, _, to, _, distance) = it.split(" ")
        distances[Pair(from, to)] = distance.toInt()
        distances[Pair(to, from)] = distance.toInt()
    }

    return distances.keys.map { it.first }.distinct().permutations().finder { perm ->
        perm.windowed(2).sumOf { distances[it.toPair()] }
    }
}

fun main() = PuzzleInput(2015, 9).withSolutions({ part1() }, { part2() }).run()