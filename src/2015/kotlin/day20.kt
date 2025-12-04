import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val input = input.toInt()
    val houses = IntArray(input / 10)
    for (i in 1 until houses.size) {
        for (j in i until houses.size step i) {
            houses[j] += i * 10
        }
    }

    return houses.indexOfFirst { it >= input }
}

private fun PuzzleInput.part2(): Any? {
    val input = input.toInt()
    val houses = IntArray(input / 10)
    for (i in 1 until houses.size) {
        for (j in i until minOf(i * 50 + 1, houses.size) step i) {
            houses[j] += i * 11
        }
    }

    return houses.indexOfFirst { it >= input }
}

fun main() = PuzzleInput(2015, 20).withSolutions({ part1() }, { part2() }).run()