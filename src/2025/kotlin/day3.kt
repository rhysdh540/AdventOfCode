import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return run(2)
}

private fun PuzzleInput.part2(): Any? {
    return run(12)
}

private fun PuzzleInput.run(k: Int): Any? {
    return lines.sumOf { line ->
        val digits = line.map { it.digitToInt() }
        val stack = mutableListOf<Int>()
        for ((i, digit) in digits.withIndex()) {
            // remove smaller numbers as long as we have enough remaining digits to fill k
            while (stack.isNotEmpty() && stack.last() < digit && stack.size + (digits.size - i) > k) {
                stack.removeLast()
            }
            stack.add(digit)
        }
        if (stack.size > k) {
            stack[k..stack.size].clear()
        }
        stack.joinToString("").toLong()
    }
}

fun main() {
    val input = PuzzleInput(2025, 3)

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