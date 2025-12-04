import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (targetX, targetY) = Regex("""\d+""").findAll(input).map { it.value }.toList().ints
    val targetIndex = (targetX + targetY - 2) * (targetX + targetY - 1) / 2 + targetY

    var code = 20151125L
    repeat(targetIndex - 1) {
        code = code * 252533 % 33554393
    }

    return code
}

private fun PuzzleInput.part2(): Any? {
    return "Merry Christmas!"
}

fun main() = PuzzleInput(2015, 25).withSolutions({ part1() }, { part2() }).run()