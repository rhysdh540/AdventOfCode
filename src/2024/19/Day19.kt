import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_19(input: String): Any? {
    val (towels, patterns) = input.split("\n\n")
    return patterns.split("\n").count { it.matches(Regex("^(${towels.replace(", ", "|")})*")) }
}

fun part2_19(input: String): Any? {
    val parts = input.split("\n\n")
    val towels = parts[0].split(", ").toSet()
    val patterns = parts[1].split("\n")

    val memo = mutableMapOf("" to 1L)

    fun count(p: String): Long = memo.getOrPut(p) {
        towels.filter { p.startsWith(it) }.sumOf { count(p.removePrefix(it)) }
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