import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_25(input: String): Any? {
    val (targetX, targetY) = Regex("""\d+""").findAll(input).map { it.value.toInt() }.toList()
    val targetIndex = (targetX + targetY - 2) * (targetX + targetY - 1) / 2 + targetY

    var code = 20151125L
    repeat(targetIndex - 1) {
        code = code * 252533 % 33554393
    }

    return code
}

@Suppress("unused")
fun part2_25(input: String): Any? {
    return "Merry Christmas!"
}

fun main() {
    val input = Path("inputs/2015/25.txt").readText()

    var start = System.nanoTime()
    var result = part1_25(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_25(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}