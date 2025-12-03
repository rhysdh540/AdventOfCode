@file:Suppress("NOTHING_TO_INLINE", "unused")

package dev.rdh.aoc

val Pair<Int, Int>.x get() = first
val Pair<Int, Int>.y get() = second
val Pair<Long, Long>.x get() = first
val Pair<Long, Long>.y get() = second

@JvmName("intPlus")
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(this.first + other.first, this.second + other.second)
@JvmName("intMinus")
operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>) = Pair(this.first - other.first, this.second - other.second)
@JvmName("intTimes")
operator fun Pair<Int, Int>.times(scalar: Int) = Pair(this.first * scalar, this.second * scalar)
@JvmName("intDiv")
operator fun Pair<Int, Int>.div(scalar: Int) = Pair(this.first / scalar, this.second / scalar)
@JvmName("intDot")
infix fun Pair<Int, Int>.dot(other: Pair<Int, Int>) = this.first * other.first + this.second * other.second

@JvmName("longPlus")
operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) = Pair(this.first + other.first, this.second + other.second)
@JvmName("longMinus")
operator fun Pair<Long, Long>.minus(other: Pair<Long, Long>) = Pair(this.first - other.first, this.second - other.second)
@JvmName("longTimes")
operator fun Pair<Long, Long>.times(scalar: Long) = Pair(this.first * scalar, this.second * scalar)
@JvmName("longDiv")
operator fun Pair<Long, Long>.div(scalar: Long) = Pair(this.first / scalar, this.second / scalar)
@JvmName("longDot")
infix fun Pair<Long, Long>.dot(other: Pair<Long, Long>) = this.first * other.first + this.second * other.second