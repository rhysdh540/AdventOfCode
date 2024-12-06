import java.io.OutputStream
import java.io.PrintStream
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1(input: String): Any? {
    val (rulesS, sequencesS) = input.split("\n\n").map { it.split("\n") }
    val rules = rulesS.map { it.split("|") }.map { it[0].toInt() to it[1].toInt() }
    val sequences = sequencesS.map { it.split(",").map { it.toInt() } }

    var sum = 0
    for (seq in sequences) {
        var valid = true
        for ((i, j) in rules) {
            if (seq.contains(i) && seq.contains(j)) {
                if (seq.indexOf(i) > seq.indexOf(j)) {
                    valid = false
                    break
                }
            }
        }

        if (valid) {
            sum += seq[seq.size / 2]
        }
    }

    return sum
}

fun part2(input: String): Any? {
    val (rulesS, sequencesS) = input.split("\n\n").map { it.split("\n") }
    val rules = rulesS.map { it.split("|") }.map { it[0].toInt() to it[1].toInt() }
    val sequences = sequencesS.map { it.split(",").map { it.toInt() } }

    var sum = 0
    for (seq in sequences) {
        var valid = true
        var x = 0
        while (x < rules.size) {
            val (i, j) = rules[x]
            if (seq.contains(i) && seq.contains(j) && seq.indexOf(i) > seq.indexOf(j)) {
                valid = false
                Collections.swap(seq, seq.indexOf(i), seq.indexOf(j))
                x = 0
            } else {
                x++
            }
        }

        if (!valid) {
            sum += seq[seq.size / 2]
        }
    }

    return sum
}

fun main() {
    val input = Path("inputs/2024/5.txt").readText()
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