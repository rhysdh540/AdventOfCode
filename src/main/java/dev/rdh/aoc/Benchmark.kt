package dev.rdh.aoc

import kotlin.math.roundToInt
import kotlin.math.sqrt

internal inline fun bench(
    name: String,
    targetTimeNs: Long,
    maxIterations: Int,
    fn: () -> Any?
): Stats {
    var iter = 1
    var elapsed = measureNs(iter, fn)

    while (elapsed < targetTimeNs && iter < maxIterations) {
        iter = (iter * 2).coerceAtMost(maxIterations)
        elapsed = measureNs(iter, fn)
    }

    val timePerIter = elapsed.toDouble() / iter
    val targetIters = (targetTimeNs * 1.2 / timePerIter).roundToInt()
        .coerceAtLeast(1)
        .coerceAtMost(maxIterations)

    val times = LongArray(targetIters)
    for (i in 0 until targetIters) {
        val start = System.nanoTime()
        fn()
        times[i] = System.nanoTime() - start
    }

    return computeStats(filterOutliers(times)).copy(name = name)
}

private fun filterOutliers(times: LongArray): LongArray {
    val stats = computeStats(times)
    val lowerBound = stats.meanNs - 3 * stats.stddevNs
    val upperBound = stats.meanNs + 3 * stats.stddevNs

    return times.filter { it.toDouble() in lowerBound..upperBound }.toLongArray()
}

private fun computeStats(times: LongArray): Stats {
    val n = times.size
    var min = Long.MAX_VALUE
    var max = Long.MIN_VALUE
    var sum = 0.0

    for (t in times) {
        if (t < min) min = t
        if (t > max) max = t
        sum += t
    }

    val mean = sum / n
    var varSum = 0.0
    for (t in times) {
        val d = t - mean
        varSum += d * d
    }

    val variance = if (n > 1) varSum / (n - 1) else 0.0
    val stddev = sqrt(variance)

    return Stats(
        name = "Benchmark",
        iterations = n,
        minNs = min.toDouble(),
        maxNs = max.toDouble(),
        meanNs = mean,
        stddevNs = stddev
    )
}

data class Stats(
    private val name: String,
    val iterations: Int,
    val minNs: Double,
    val maxNs: Double,
    val meanNs: Double,
    val stddevNs: Double
) {
    override fun toString() = buildString {
        append("$name: ")
        append("n=${iterations} ")
        append("mean=${ftime(meanNs)} ")
        append("min=${ftime(minNs)} ")
        append("max=${ftime(maxNs)} ")
        append("stddev=${ftime(stddevNs)} ")
    }
}

private inline fun measureNs(iterations: Int, fn: () -> Any?): Long {
    val start = System.nanoTime()
    repeat(iterations) { fn() }
    return System.nanoTime() - start
}

private fun ftime(timeNs: Double): String {
    val times = mapOf(
        "h" to 3.6e12, // if this ever gets used i will have concerns
        "m" to 6e10,
        "s" to 1e9,
        "ms" to 1e6,
        "µs" to 1e3
    )

    for ((unit, factor) in times) {
        val t = timeNs / factor
        if (t >= 1) {
            return "%.3f%s".format(t, unit)
        }
    }
    return "%.3fns".format(timeNs)
}