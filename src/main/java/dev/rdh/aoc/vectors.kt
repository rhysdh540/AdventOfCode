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
@JvmName("intUnaryMinus")
operator fun Pair<Int, Int>.unaryMinus() = v(-this.x, -this.y)
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
@JvmName("longUnaryMinus")
operator fun Pair<Long, Long>.unaryMinus() = v(-this.x, -this.y)
@JvmName("longDot")
infix fun Pair<Long, Long>.dot(other: Pair<Long, Long>) = this.x * other.x + this.y * other.y

operator fun <T> List<List<T>>.get(x: Int, y: Int) = this[y][x]
operator fun <T> MutableList<MutableList<T>>.set(x: Int, y: Int, value: T) {
    this[y][x] = value
}

operator fun <T> List<List<T>>.get(pos: Pair<Int, Int>) = this[pos.y][pos.x]
operator fun <T> MutableList<MutableList<T>>.set(pos: Pair<Int, Int>, value: T) {
    this[pos.y][pos.x] = value
}

object Vectors {
    val ZERO = v(0, 0)

    val d4 = listOf(
        v(0, -1),
        v(1, 0),
        v(0, 1),
        v(-1, 0)
    )

    val d8 = listOf(
        v(-1, -1), v(0, -1), v(1, -1),
        v(-1, 0),            v(1, 0),
        v(-1, 1),  v(0, 1),  v(1, 1)
    )
}