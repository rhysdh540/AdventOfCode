import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_13(input: String): Any? {
    return input.trim().split("\n\n").map { Day13.parseInput(it, 0) }.sumOf { it.solve() }
}

fun part2_13(input: String): Any? {
    return input.trim().split("\n\n").map { Day13.parseInput(it, 10_000_000_000_000) }.sumOf { it.solve() }
}

object Day13 {
    data class ClawMachine(val aX: Int, val aY: Int, val bX: Int, val bY: Int, val pX: Long, val pY: Long) {
        fun solve(): Long {
            val d = aX.toLong() * bY.toLong() - aY.toLong() * bX.toLong()
            if (d == 0L) {
                // since the determinant of the matrix is 0, the equations are linearly dependent
                // and cramer's rule doesn't apply
                return 0
            }

            // cramer's rule
            // [aX bX] [n1] = [pX]
            // [aY bY] [n2] = [pY]
            // essentially:
            // n1 = [pX bX] / d
            //      [pY bY]
            // n2 = [aX pX] / d
            //      [aY pY]
            val d1 = pX * bY - pY * bX
            val d2 = aX * pY - aY * pX

            if (d1 % d != 0L || d2 % d != 0L) {
                // can't push a button a fractional number of times
                return 0
            }

            val aPresses = d1 / d
            val bPresses = d2 / d

            if (aPresses < 0 || bPresses < 0) {
                // can't push a button a negative number of times
                return 0
            }

            return 3 * aPresses + bPresses
        }
    }

    fun parseInput(input: String, prizeOffset: Long): ClawMachine {
        val lines = input.lines()
        val aMatch = Regex("Button A: X\\+(-?\\d+), Y\\+(-?\\d+)").find(lines[0])!!
        val bMatch = Regex("Button B: X\\+(-?\\d+), Y\\+(-?\\d+)").find(lines[1])!!
        val prizeMatch = Regex("Prize: X=(-?\\d+), Y=(-?\\d+)").find(lines[2])!!

        return ClawMachine(
            aX = aMatch.groupValues[1].toInt(),
            aY = aMatch.groupValues[2].toInt(),
            bX = bMatch.groupValues[1].toInt(),
            bY = bMatch.groupValues[2].toInt(),
            pX = prizeMatch.groupValues[1].toLong() + prizeOffset,
            pY = prizeMatch.groupValues[2].toLong() + prizeOffset
        )
    }
}

fun main() {
    val input = Path("inputs/2024/13.txt").readText()

    var start = System.nanoTime()
    var result = part1_13(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_13(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}