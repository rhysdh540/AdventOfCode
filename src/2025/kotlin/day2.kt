import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return splitBy(",").sumOf {
        val range = it.split("-").longs.let { r -> r.first()..r.last() }
        var total = 0L
        for (d in 1..(range.last.numDigits / 2)) {
            // every invalid id has the form n = k * 10^d + k = k(10^d + 1)

            val m = 10.pow(d) + 1 // n = km, as above

            // in order to have d digits repeated, n has to have 2d digits
            // so k must have exactly d digits
            val loK = 10.pow(d - 1)
            val hiK = 10.pow(d) - 1

            // given all this, we want to find all n in range with 2d digits
            // L <= mk <= R ==> L/m <= k <= R/m
            // then combine with loK <= k <= hiK
            val a = range.first.ceilDiv(m).coerceAtLeast(loK)
            val b = range.last.floorDiv(m).coerceAtMost(hiK)

            // if a > b, then there are no valid k in range
            // if a <= b, then every k in [a, b] is valid
            if (a <= b) {
                // Σn = Σmk = mΣk from k=a to b
                total += m * (a..b).sum()
            }
        }
        total
    }
}

private fun PuzzleInput.part2(): Any? {
    return splitBy(",").sumOf {
        val range = it.split("-").longs.let { r -> r.first()..r.last() }
        // here, an invalid id has the form n = k * (10^{d(t-1)} + 10^{d(t-2)} + ... + 10^d + 1) for t >= 2
        // which is a geometric series summing to n = k * (10^{dt} - 1) / (10^d - 1)
        // so let's say that C(d,t) = (10^{dt} - 1) / (10^d - 1), and n=kC(d,t)
        // given L <= n <= R, we have L/C(d,t) <= k <= R/C(d,t), and all k in that range are valid

        val seen = mutableSetOf<Long>()
        val maxDigits = range.last.numDigits
        for (d in 1..maxDigits) {
            val maxT = maxDigits / d
            if (maxT < 2) continue // need at least two repeats

            val step = 10.pow(d)
            val loK = 10.pow(d - 1)
            val hiK = 10.pow(d) - 1

            for (t in 2..maxT) {
                val c = (step.pow(t) - 1) / (step - 1)
                val nMin = loK * c
                if (nMin > range.last) break // k is only going to get larger, so stop here
                val a = range.first.ceilDiv(c).coerceAtLeast(loK)
                val b = range.last.floorDiv(c).coerceAtMost(hiK)

                if (a <= b) {
                    // TODO: find some way to avoid checking every number here
                    //  according to my math professor some form of inclusion-exclusion could work
                    for (k in a..b) {
                        seen.add(c * k)
                    }
                }
            }
        }
        seen.sum()
    }
}

fun main() = PuzzleInput(2025, 2).withSolutions({ part1() }, { part2() }).run()