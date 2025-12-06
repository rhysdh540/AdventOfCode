import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return lines.map { it.spaced }.rotateCounterClockwise().sumOf {
        val op = it.last()
        val operands = it.dropLast(1).longs
        when (op) {
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
    while(true) {
        val op = ops[base]
        var i = base + 1
        while(i < len && (ops.getOrNull(i) ?: ' ') == ' ') {
            i++
        }

        if (i >= ops.length) {
            i = vs.maxOf { it.length } + 1
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
        if (base >= ops.length) break
    }

    return sum
}

fun main() = PuzzleInput(2025, 6).withSolutions({ part1() }, { part2() }).run()