import java.io.OutputStream
import java.io.PrintStream
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_5(input: String): Any? {
    val (rules, sequences) = input.split("\n\n").map {
        it.lines().map {
            it.split(Regex("[|,]")).map { it.toInt() }
        }
    }

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

fun part2_5(input: String): Any? {
    val (rulesS, sequencesS) = input.split("\n\n").map { it.split("\n") }
    val rules = rulesS.map { it.split("|") }.map { it[0].toInt() to it[1].toInt() }
    val sequences = sequencesS.map { it.split(",").map { it.toInt() } }

    var sum = 0
    for (seq in sequences) {
        var valid = true
        var x = 0
        while (x < rules.size) {
            val (i, j) = rules[x]
            val i1 = seq.indexOf(i)
            val i2 = seq.indexOf(j)
            if (i1 != -1 && i2 != -1 && i1 > i2) {
                valid = false
                Collections.swap(seq, i1, i2)
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
    part1_5(input); part2_5(input)
    System.setOut(o)

    var start = System.nanoTime()
    var result = part1_5(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_5(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}