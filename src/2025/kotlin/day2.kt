import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return splitBy(",").sumOf {
        val range = it.split("-").longs.let { r -> r.first()..r.last() }
        var total = 0L
        for (d in 1..(range.last.numDigits / 2)) {
            // every "interesting" number has the form n = k * 10^d + k = k(10^d + 1)

            val m = 10.pow(d) + 1 // n = km, as above

            // in order to have d digits repeated, n has to have 2d digits
            // so k must have exactly d digits
            val loK = 10.pow(d - 1)
            val hiK = 10.pow(d) - 1

            // given all this, we want to find all n in range with 2d digits
            // L <= mk <= R ==> L/m <= k <= R/m
            // then combine with loK <= k <= hiK
            val a = Math.ceilDiv(range.first, m).coerceAtLeast(loK)
            val b = Math.floorDiv(range.last, m).coerceAtMost(hiK)

            // if a > b, then there are no valid k in range
            // if a <= b, then every k in [a, b] is valid
            if (a <= b) {
                // Σn = Σmk = mΣk from k=a to b
                // which is just an arithmetic series
                val sumK = (b - a + 1) * (a + b) / 2
                total += m * sumK
            }
        }
        total
    }
}

private fun PuzzleInput.part2(): Any? {
    // can't figure out how to do this mathematically yet
    return splitBy(",").sumOf {
        val range = it.split("-").longs.let { r -> r.first()..r.last() }
        var sum = 0L
        for (num in range) {
            val str = num.toString()
            val len = str.length

            for (m in 1 until len) {
                if (len % m == 0) {
                    val base = str.take(m)
                    val repeats = len / m
                    if (repeats >= 2 && str == base.repeat(repeats)) {
                        sum += num
                        break
                    }
                }
            }
        }
        sum
    }
}

fun main() {
    val input = getInput(2025, 2)

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