import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_8(input: String): Any? {
    return input.lines().sumOf {
        val mem = it.replace("\\\\", "x").replace("\\\"", "x").replace(Regex("""\\x[0-9a-f]{2}"""), "x").length - 2
        it.length - mem
    }
}

fun part2_8(input: String): Any? {
    return input.lines().sumOf {
        it.count { it == '\\' || it == '"' } + 2
    }
}

fun main() {
    val input = Path("inputs/2015/8.txt").readText()

    var start = System.nanoTime()
    var result = part1_8(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_8(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}