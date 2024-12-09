import java.security.MessageDigest
import java.util.stream.IntStream
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_4(input: String): Any? {
    return run(input, "00000")
}

fun part2_4(input: String): Any? {
    return run(input, "000000")
}

fun run(input: String, match: String): Int {
    return IntStream.iterate(0) { i -> i + 1 }.parallel().filter { i ->
        val md = MessageDigest.getInstance("MD5")
        val hash = md.digest((input + i).toByteArray())
        hash.joinToString("") { "%02x".format(it) }.startsWith(match)
    }.findFirst().asInt
}

fun main() {
    val input = Path("inputs/2015/4.txt").readText()

    var start = System.nanoTime()
    var result = part1_4(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_4(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}