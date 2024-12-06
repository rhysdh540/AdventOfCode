import java.io.OutputStream
import java.io.PrintStream
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1(input: String): Any? {

}

fun part2(input: String): Any? {
    return "Not implemented"
}

fun main() {
    val input = Path("inputs/{{year}}/{{day}}.txt").readText()
    var o = System.out
    System.setOut(PrintStream(OutputStream.nullOutputStream()))
    part1(input); part2(input)
    System.setOut(o)

    var start = System.nanoTime()
    var result = part1(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}