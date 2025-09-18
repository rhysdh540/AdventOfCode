import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return Regex("""mul\((\d+),(\d+)\)""").findAll(input).map { it.destructured }.sumOf { (a, b) ->
        a.toInt() * b.toInt()
    }
}

private fun PuzzleInput.part2(): Any? {
    return Regex("""(mul|do|don't)\((?:(\d+),(\d+))?\)""").findAll(input).map { it.destructured }
        .fold(0 to true) { (acc, enabled), (op, a, b) ->
            return@fold when (op) {
                "do" -> acc to true
                "don't" -> acc to false
                "mul" -> {
                    if (enabled) {
                        val a = a.toInt()
                        val b = b.toInt()
                        acc + a * b to true
                    } else {
                        acc to false
                    }
                }

                else -> error("Invalid input")
            }
        }.first
}

fun main() {
    val input = getInput(2024, 3)

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