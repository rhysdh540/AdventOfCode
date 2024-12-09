import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_10(input: String): Any? {

}

fun part2_10(input: String): Any? {
    return "Not implemented"
}

fun main() {
    val input = Path("inputs/2024/10.txt").readText()

    var start = System.nanoTime()
    var result = part1_10(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_10(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}