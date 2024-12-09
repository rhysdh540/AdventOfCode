import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_5(input: String): Any? {
    return input.split("\n").count {
        val vowels = it.count { c -> c in "aeiou" }
        val double = it.zipWithNext().any { (a, b) -> a == b }
        val forbidden = it.contains("ab") || it.contains("cd") || it.contains("pq") || it.contains("xy")
        vowels >= 3 && double && !forbidden
    }
}

fun part2_5(input: String): Any? {
    return input.split("\n").count {
        val pair = it.matches(Regex(""".*(..).*\1.*"""))
        val repeat = it.matches(Regex(""".*(.).\1.*"""))
        pair && repeat
    }
}

fun main() {
    val input = Path("inputs/2015/5.txt").readText()

    var start = System.nanoTime()
    var result = part1_5(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_5(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}