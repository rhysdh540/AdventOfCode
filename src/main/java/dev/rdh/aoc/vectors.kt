@file:Suppress("NOTHING_TO_INLINE", "unused")

package dev.rdh.aoc

val Pair<Int, Int>.x get() = first
val Pair<Int, Int>.y get() = second
val Pair<Long, Long>.x get() = first
val Pair<Long, Long>.y get() = second

fun v(x: Int, y: Int) = Pair(x, y)
fun v(x: Long, y: Long) = Pair(x, y)

@JvmName("intPlus")
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = v(this.x + other.x, this.y + other.y)
@JvmName("intMinus")
operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>) = v(this.x - other.x, this.y - other.y)
@JvmName("intTimes")
operator fun Pair<Int, Int>.times(scalar: Int) = v(this.x * scalar, this.y * scalar)
@JvmName("intDiv")
operator fun Pair<Int, Int>.div(scalar: Int) = v(this.x / scalar, this.y / scalar)
@JvmName("intDot")
infix fun Pair<Int, Int>.dot(other: Pair<Int, Int>) = this.x * other.x + this.y * other.y

@JvmName("longPlus")
operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) = v(this.x + other.x, this.y + other.y)
@JvmName("longMinus")
operator fun Pair<Long, Long>.minus(other: Pair<Long, Long>) = v(this.x - other.x, this.y - other.y)
@JvmName("longTimes")
operator fun Pair<Long, Long>.times(scalar: Long) = v(this.x * scalar, this.y * scalar)
@JvmName("longDiv")
operator fun Pair<Long, Long>.div(scalar: Long) = v(this.x / scalar, this.y / scalar)
@JvmName("longDot")
infix fun Pair<Long, Long>.dot(other: Pair<Long, Long>) = this.x * other.x + this.y * other.y