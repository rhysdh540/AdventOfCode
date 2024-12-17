import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_6(input: String): Any? {
    val n = input.lines().first().length
    val columns = List(n) { mutableListOf<Char>() }
    input.lines().forEach {
        for(i in 0..<n) {
            columns[i].add(it[i])
        }
    }

    return columns.map { it.groupingBy { it }.eachCount().maxBy { it.value }.key }.joinToString("")
}

fun part2_6(input: String): Any? {
    val n = input.lines().first().length
    val columns = List(n) { mutableListOf<Char>() }
    input.lines().forEach {
        for(i in 0..<n) {
            columns[i].add(it[i])
        }
    }

    return columns.map { it.groupingBy { it }.eachCount().minBy { it.value }.key }.joinToString("")
}

fun main() {
    val input = Path("inputs/2016/6.txt").readText()

    var start = System.nanoTime()
    var result = part1_6(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_6(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}