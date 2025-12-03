import dev.rdh.aoc.*
import java.util.Collections

private fun PuzzleInput.part1(): Any? {
    val (rules, sequences) = sections.map {
        it.lines().map {
            it.split(Regex("[|,]")).ints
        }
    }

    return sequences.filter {
        rules.none { (i, j) -> it.contains(i) && it.contains(j) && it.indexOf(i) > it.indexOf(j) }
    }.sumOf { it[it.size / 2] }
}

private fun PuzzleInput.part2(): Any? {
    val (rulesS, sequencesS) = sections.map { it.split("\n") }
    val rules = rulesS.map { it.split("|") }.map { it.ints.toPair() }
    val sequences = sequencesS.map { it.split(",").ints }

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

fun main() = PuzzleInput(2024, 5).withSolutions({ part1() }, { part2() }).run()