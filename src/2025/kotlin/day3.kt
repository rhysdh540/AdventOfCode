import dev.rdh.aoc.*
import kotlin.math.min

private fun PuzzleInput.part1() = run(2)
private fun PuzzleInput.part2() = run(12)

private fun PuzzleInput.run(k: Int): Any? {
    val size = lines[0].length
    val stack = IntArray(size)
    return lines.sumOf { line ->
        var ptr = 0
        for (i in line.indices) {
            val digit = line[i] - '0'
            // remove smaller numbers as long as we have enough remaining digits to fill k
            while (ptr > 0 && stack[ptr - 1] < digit && ptr + (size - i) > k) {
                ptr--
            }
            stack[ptr++] = digit
        }
        (0 until min(ptr, k)).fold(0L) { acc, i -> acc * 10 + stack[i] }
    }
}

fun main() = PuzzleInput(2025, 3).withSolutions({ part1() }, { part2() }).run()