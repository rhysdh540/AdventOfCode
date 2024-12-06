import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_{{day}}(input: String): Any? {

}

fun part2_{{day}}(input: String): Any? {
    return "Not implemented"
}

fun main() {
    val input = Path("inputs/{{year}}/{{day}}.txt").readText()

    var start = System.nanoTime()
    var result = part1_{{day}}(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_{{day}}(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}