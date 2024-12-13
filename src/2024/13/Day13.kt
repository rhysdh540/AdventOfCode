import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_13(input: String): Any? {
    data class ClawMachine(val aX: Int, val aY: Int, val bX: Int, val bY: Int, val prizeX: Int, val prizeY: Int)

    val parsed = input.trim().split("\n\n").map { section ->
        val lines = section.lines()
        val aMatch = Regex("Button A: X\\+(-?\\d+), Y\\+(-?\\d+)").find(lines[0])!!
        val bMatch = Regex("Button B: X\\+(-?\\d+), Y\\+(-?\\d+)").find(lines[1])!!
        val prizeMatch = Regex("Prize: X=(-?\\d+), Y=(-?\\d+)").find(lines[2])!!

        ClawMachine(
            aX = aMatch.groupValues[1].toInt(),
            aY = aMatch.groupValues[2].toInt(),
            bX = bMatch.groupValues[1].toInt(),
            bY = bMatch.groupValues[2].toInt(),
            prizeX = prizeMatch.groupValues[1].toInt(),
            prizeY = prizeMatch.groupValues[2].toInt()
        )
    }

    return parsed.sumOf { (aX, aY, bX, bY, pX, pY) ->
        (0..100).flatMap { aPresses ->
            (0..100).mapNotNull { bPresses ->
                val x = aPresses * aX + bPresses * bX
                val y = aPresses * aY + bPresses * bY

                if (x == pX && y == pY) {
                    3 * aPresses + bPresses
                } else {
                    null
                }
            }
        }.firstOrNull() ?: 0
    }
}

fun part2_13(input: String): Any? {
    data class ClawMachine(val aX: Int, val aY: Int, val bX: Int, val bY: Int, val pX: Long, val pY: Long)

    val parsed = input.trim().split("\n\n").map { section ->
        val lines = section.lines()
        val aMatch = Regex("Button A: X\\+(-?\\d+), Y\\+(-?\\d+)").find(lines[0])!!
        val bMatch = Regex("Button B: X\\+(-?\\d+), Y\\+(-?\\d+)").find(lines[1])!!
        val prizeMatch = Regex("Prize: X=(-?\\d+), Y=(-?\\d+)").find(lines[2])!!

        ClawMachine(
            aX = aMatch.groupValues[1].toInt(),
            aY = aMatch.groupValues[2].toInt(),
            bX = bMatch.groupValues[1].toInt(),
            bY = bMatch.groupValues[2].toInt(),
            pX = prizeMatch.groupValues[1].toLong() + 10_000_000_000_000L,
            pY = prizeMatch.groupValues[2].toLong() + 10_000_000_000_000L
        )
    }

    // finds the greatest common divisor of a and b, and the coefficients x and y
    fun gcde(a: Int, b: Int): Triple<Int, Long, Long> {
        if (b == 0) return Triple(a, 1L, 0L)
        val (gcd, x1, y1) = gcde(b, a % b)
        return Triple(gcd, y1, x1 - (a / b) * y1)
    }

    // finds a linear combination of a and b that sums to c (if possible)
    fun diophantine(a: Int, b: Int, c: Long): Pair<Long, Long>? {
        val (gcd, x0, y0) = gcde(a, b)
        if (c % gcd != 0L) return null
        val scale = c / gcd
        return Pair(x0 * scale, y0 * scale)
    }

    return parsed.sumOf { (aX, aY, bX, bY, pX, pY) ->
        val xS = diophantine(aX, bX, pX)
        val yS = diophantine(aY, bY, pY)

        if (xS == null || yS == null) return@sumOf 0
        val d = aX.toLong() * bY.toLong() - aY.toLong() * bX.toLong()
        if (d == 0L) {
            // d == 0, cramer's rule doesn't apply
            return@sumOf 0
        }

        // cramer's rule - find the determinant of the matrix formed by the coefficients of the linear equations
        // [aX bX] [x] = [pX]
        // [aY bY] [y] = [pY]
        // if the determinant is 0, they are linearly dependent, and there is no solution
        val d1 = pX * bY - pY * bX
        val d2 = aX.toLong() * pY - aY.toLong() * pX

        if (d1 % d != 0L || d2 % d != 0L) {
            // no integer solution
            return@sumOf 0
        }

        val aPresses = d1 / d
        val bPresses = d2 / d

        if (aPresses < 0 || bPresses < 0) {
            return@sumOf 0
        }

        return@sumOf 3 * aPresses + bPresses
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