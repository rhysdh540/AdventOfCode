import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return lines.sumOf {
        (it.first { it.isDigit() }.digitToInt() * 10) + it.last { it.isDigit() }.digitToInt()
    }
}

private fun PuzzleInput.part2(): Any? {
    val digits = mapOf(
        "one" to "o1e",
        "two" to "t2o",
        "three" to "t3e",
        "four" to "f4r",
        "five" to "f5e",
        "six" to "s6x",
        "seven" to "s7n",
        "eight" to "e8t",
        "nine" to "n9e",
        "zero" to "z0o"
    )

    var fixed = input
    digits.forEach { (key, value) ->
        fixed = fixed.replace(key, value)
    }

    return PuzzleInput(fixed).part1()
}

fun main() = PuzzleInput(2023, 1).withSolutions({ part1() }, { part2() }).run()