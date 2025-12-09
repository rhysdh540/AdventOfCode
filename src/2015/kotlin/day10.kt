import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return solve(40)
}

private fun PuzzleInput.part2(): Any? {
    return solve(50)
}

private fun PuzzleInput.solve(n: Int): Int {
    val regex = Regex("""(\d)\1*""")
    var result = input
    repeat(n) {
        result = regex.findAll(result).joinToString("") {
            "${it.value.length}${it.value[0]}"
        }
    }

    return result.length
}

fun main() = PuzzleInput(2015, 10).withSolutions({ part1() }, { part2() }).run()