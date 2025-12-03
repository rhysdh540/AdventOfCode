@file:Suppress("NOTHING_TO_INLINE")

package dev.rdh.aoc

import java.math.BigInteger
import kotlin.math.abs

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

fun IntRange.sum(): Long {
    val n = this.last - this.first + 1
    return n.toLong() * (this.first + this.last) / 2
}

fun LongRange.sum(): Long {
    val n = this.last - this.first + 1
    return n * (this.first + this.last) / 2
}

inline infix fun Int.ceilDiv(divisor: Int) = Math.ceilDiv(this, divisor)
inline infix fun Long.ceilDiv(divisor: Long) = Math.ceilDiv(this, divisor)
inline infix fun Int.floorDiv(divisor: Int) = Math.floorDiv(this, divisor)
inline infix fun Long.floorDiv(divisor: Long) = Math.floorDiv(this, divisor)
inline infix fun Int.floorMod(modulus: Int) = Math.floorMod(this, modulus)
inline infix fun Long.floorMod(modulus: Long) = Math.floorMod(this, modulus)

inline operator fun BigInteger.plus(other: BigInteger): BigInteger = this.add(other)
inline operator fun BigInteger.minus(other: BigInteger): BigInteger = this.subtract(other)
inline operator fun BigInteger.times(other: BigInteger): BigInteger = this.multiply(other)
inline operator fun BigInteger.div(other: BigInteger): BigInteger = this.divide(other)
inline operator fun BigInteger.rem(other: BigInteger): BigInteger = this.remainder(other)
inline operator fun BigInteger.unaryMinus(): BigInteger = this.negate()