import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_19(input: String): Any? {
    val parts = input.split("\n\n")
    val towels = parts[0].split(", ").toSet()
    val patterns = parts[1].split("\n")

    fun valid(pattern: String): Boolean {
        if(pattern.isEmpty()) return true
        return towels.any { pattern.startsWith(it) && valid(pattern.removePrefix(it)) }
    }

    return patterns.count { valid(it) }
}

fun part2_19(input: String): Any? {
    val parts = input.split("\n\n")
    val towels = parts[0].split(", ").toSet()
    val patterns = parts[1].split("\n")

    val memo = mutableMapOf<String, Long>()

    fun count(p: String): Long {
        if(p.isEmpty()) return 1
        memo[p]?.let { return it }

        val c = towels.filter { p.startsWith(it) }.sumOf { count(p.removePrefix(it)) }

        memo[p] = c
        return c
    }

    return patterns.sumOf { count(it) }
}

fun main() {
    val input = Path("inputs/2024/19.txt").readText()

    var start = System.nanoTime()
    var result = part1_19(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_19(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}