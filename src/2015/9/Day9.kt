import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_9(input: String): Any? {
    val distances = mutableMapOf<Pair<String, String>, Int>()
    input.lines().forEach {
        val (from, _, to, _, distance) = it.split(" ")
        distances[Pair(from, to)] = distance.toInt()
        distances[Pair(to, from)] = distance.toInt()
    }

    val permutations = distances.keys.map { it.first }.distinct().permutations()
    return permutations.minOf { perm ->
        perm.windowed(2).sumOf { distances[Pair(it[0], it[1])]!! }
    }
}

fun part2_9(input: String): Any? {
    val distances = mutableMapOf<Pair<String, String>, Int>()
    input.lines().forEach {
        val (from, _, to, _, distance) = it.split(" ")
        distances[Pair(from, to)] = distance.toInt()
        distances[Pair(to, from)] = distance.toInt()
    }

    val permutations = distances.keys.map { it.first }.distinct().permutations()
    return permutations.maxOf { perm ->
        perm.windowed(2).sumOf { distances[Pair(it[0], it[1])]!! }
    }
}

fun <T> List<T>.permutations(): List<List<T>> {
    if (size == 1) return listOf(this)
    val perms = mutableListOf<List<T>>()
    val toInsert = first()
    for (perm in drop(1).permutations()) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, toInsert)
            perms.add(newPerm)
        }
    }
    return perms
}

fun main() {
    val input = Path("inputs/2015/9.txt").readText()

    var start = System.nanoTime()
    var result = part1_9(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_9(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}