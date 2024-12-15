import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

fun part1_2(input: String): Any? {
    return input.lines().map { it.split(' ').map { it.toInt() } }.count {
        (it == it.sorted() || it == it.sortedDescending()) && it.zipWithNext().all { abs(it.first - it.second) in 1..3 }
    }
}

fun part2_2(input: String): Any? {
    fun allowed(f: List<Int>) = (f == f.sorted() || f == f.sortedDescending()) && f.zipWithNext().all { abs(it.first - it.second) in 1..3 }
    return input.lines().map { it.split(' ').map { it.toInt() } }.count { l ->
        allowed(l) || l.indices.any { allowed((l as MutableList<Int>).apply { removeAt(it) }) }
    }
}

fun main() {
    val input = Path("inputs/2024/2.txt").readText()

    var start = System.nanoTime()
    var result = part1_2(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_2(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}