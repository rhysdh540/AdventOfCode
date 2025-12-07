import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return lines.map { it.spaced }.rotateCounterClockwise().sumOf {
        val operands = it.dropLast(1).longs
        when (val op = it.last()) {
            "+" -> operands.sum()
            "*" -> operands.product()
            else -> error(op)
        }
    }
}

private fun PuzzleInput.part2(): Any? {
    val vs = lines.dropLast(1)
    val ops = lines.last()

    val len = vs.maxOf { it.length } + 1
    var sum = 0L

    var base = 0
    while(base < len) {
        val op = ops[base]
        var i = base + 1
        while(i < len && (ops.getOrNull(i) ?: ' ') == ' ') { // because editors will remove trailing spaces :(
            i++
        }

        val v = (0 until (i - base - 1)).map { j ->
            vs.joinToString("") { (it.getOrNull(base + j) ?: ' ').toString() }
                .replace(" ", "").toLong()
        }

        sum += when (op) {
            '+' -> v.sum()
            '*' -> v.product()
            else -> error(op)
        }

        base = i
    }

    return sum
}

fun main() = PuzzleInput(2025, 6).withSolutions({ part1() }, { part2() }).run()