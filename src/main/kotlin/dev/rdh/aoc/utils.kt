package dev.rdh.aoc

import kotlin.math.abs
import kotlin.math.log10

val Iterable<String>.ints get() = map { it.toInt() }
val Iterable<String>.longs get() = map { it.toLong() }
val String.spaced get() = split(' ')
const val blankLines = "\n\n"

fun <T> Iterable<Iterable<T>>.rotateClockwise(): List<List<T>> {
    return List(this.first().count()) { i ->
        List(this.count()) { j ->
            this.elementAt(this.count() - 1 - j).elementAt(i)
        }
    }
}

fun <T> Iterable<Iterable<T>>.rotateCounterClockwise(): List<List<T>> {
    return List(this.first().count()) { i ->
        List(this.count()) { j ->
            this.elementAt(j).elementAt(this.first().count() - 1 - i)
        }
    }
}

@JvmName("rotateClockwiseString")
fun Iterable<String>.rotateClockwise(): List<String> {
    return List(this.first().length) { i ->
        String(List(this.count()) { j ->
            this.elementAt(this.count() - 1 - j)[i]
        }.toCharArray())
    }
}

@JvmName("rotateCounterClockwiseString")
fun Iterable<String>.rotateCounterClockwise(): List<String> {
    return List(this.first().length) { i ->
        String(List(this.count()) { j ->
            this.elementAt(j)[this.first().length - 1 - i]
        }.toCharArray())
    }
}

fun <T> Iterable<Iterable<T>>.deepToMutableList(): MutableList<MutableList<T>> {
    return map { it.toMutableList() }.toMutableList()
}

@JvmName("intPlus")
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(this.first + other.first, this.second + other.second)
@JvmName("intMinus")
operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>) = Pair(this.first - other.first, this.second - other.second)
@JvmName("intTimes")
operator fun Pair<Int, Int>.times(scalar: Int) = Pair(this.first * scalar, this.second * scalar)
@JvmName("intDiv")
operator fun Pair<Int, Int>.div(scalar: Int) = Pair(this.first / scalar, this.second / scalar)

@JvmName("longPlus")
operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) = Pair(this.first + other.first, this.second + other.second)
@JvmName("longMinus")
operator fun Pair<Long, Long>.minus(other: Pair<Long, Long>) = Pair(this.first - other.first, this.second - other.second)
@JvmName("longTimes")
operator fun Pair<Long, Long>.times(scalar: Long) = Pair(this.first * scalar, this.second * scalar)
@JvmName("longDiv")
operator fun Pair<Long, Long>.div(scalar: Long) = Pair(this.first / scalar, this.second / scalar)

infix fun Int.gcd(other: Int): Int = if (other == 0) abs(this) else other gcd (this % other)
infix fun Long.gcd(other: Long): Long = if (other == 0L) abs(this) else other gcd (this % other)
infix fun Int.lcm(other: Int): Int = (this / (this gcd other)) * other
infix fun Long.lcm(other: Long): Long = (this / (this gcd other)) * other

fun Int.pow(exp: Int): Long {
    var result = 1L
    repeat(exp) {
        result *= this
    }
    return result
}

fun Long.pow(exp: Int): Long {
    var result = 1L
    repeat(exp) {
        result *= this
    }
    return result
}

val Int.numDigits: Int
    get() = toLong().numDigits

val Long.numDigits: Int
    get() {
        if (this == 0L) return 1
        var count = 0
        var n = abs(this)
        while (n > 0) {
            n /= 10
            count++
        }
        return count
    }