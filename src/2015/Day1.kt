import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_1(input: String): Any? {
    return input.count { it == '(' } - input.count { it == ')' } + 1
}

fun part2_1(input: String): Any? {
    var (floor, i) = Pair(0, 0)
    for(c in input) {
        floor += if(c == '(') 1 else -1
        if(floor == -1) {
            return i + 1
        }

        i++
    }

    throw AssertionError()
}

fun main() {
    val input = Path("inputs/2015/1.txt").readText()

    var start = System.nanoTime()
    var result = part1_1(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_1(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}