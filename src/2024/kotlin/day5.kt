import dev.rdh.aoc.*
import java.util.Collections

private fun PuzzleInput.part1(): Any? {
    val (rules, sequences) = sections.map {
        it.lines().map {
            it.split(Regex("[|,]")).map { it.toInt() }
        }
    }

    return sequences.filter {
        rules.none { (i, j) -> it.contains(i) && it.contains(j) && it.indexOf(i) > it.indexOf(j) }
    }.sumOf { it[it.size / 2] }
}

private fun PuzzleInput.part2(): Any? {
    val (rulesS, sequencesS) = sections.map { it.split("\n") }
    val rules = rulesS.map { it.split("|") }.map { it[0].toInt() to it[1].toInt() }
    val sequences = sequencesS.map { it.split(",").map { it.toInt() } }

    return sequences.filter { seq ->
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

        !valid
    }.sumOf { it[it.size / 2] }
}

fun main() {
    val input = PuzzleInput(2024, 5)

    var start = System.nanoTime()
    var result = input.part1()
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = input.part2()
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}