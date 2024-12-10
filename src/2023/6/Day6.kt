import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.sqrt

fun part1_6(input: String): Any? {
    val (times, distances) = input.lines().map {
        it.substring(it.indexOf(':') + 1).split(' ').filter { it.isNotEmpty() }.map { it.toInt() }
    }

    return (times zip distances).map { (time, distance) -> Day6.calculate(time.toLong(), distance.toLong()) }
        .fold(1L) { acc, i -> acc * i }
}

fun part2_6(input: String): Any? {
    val (time, distance) = input.lines().map {
        it.substring(it.indexOf(':') + 1).replace(" ", "").toLong()
    }

    return Day6.calculate(time, distance)
}

object Day6 {
    fun calculate(time: Long, distance: Long): Long {
        val discriminant = sqrt((time * time - 4 * distance).toDouble())
        val root1 = ((time + discriminant) / 2).toLong()
        val root2 = ((time - discriminant) / 2).toLong()
        return root1 - root2
    }
}

fun main() {
    val input = Path("inputs/2023/6.txt").readText()

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