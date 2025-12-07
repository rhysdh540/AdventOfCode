@file:Suppress("NOTHING_TO_INLINE", "unused")

package dev.rdh.aoc

data class Vec2i(val x: Int, val y: Int) {
    operator fun plus(other: Vec2i) = Vec2i(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vec2i) = Vec2i(this.x - other.x, this.y - other.y)
    operator fun times(scalar: Int) = Vec2i(this.x * scalar, this.y * scalar)
    operator fun div(scalar: Int) = Vec2i(this.x / scalar, this.y / scalar)
    operator fun unaryMinus() = Vec2i(-this.x, -this.y)
    infix fun dot(other: Vec2i) = this.x * other.x + this.y * other.y

    companion object {
        val ZERO = v(0, 0)
    }
}

data class Vec2l(val x: Long, val y: Long) {
    operator fun plus(other: Vec2l) = Vec2l(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vec2l) = Vec2l(this.x - other.x, this.y - other.y)
    operator fun times(scalar: Long) = Vec2l(this.x * scalar, this.y * scalar)
    operator fun div(scalar: Long) = Vec2l(this.x / scalar, this.y / scalar)
    operator fun unaryMinus() = Vec2l(-this.x, -this.y)
    infix fun dot(other: Vec2l) = this.x * other.x + this.y * other.y

    companion object {
        val ZERO = v(0L, 0L)
    }
}

data class Vec2d(val x: Double, val y: Double) {
    operator fun plus(other: Vec2d) = Vec2d(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vec2d) = Vec2d(this.x - other.x, this.y - other.y)
    operator fun times(scalar: Double) = Vec2d(this.x * scalar, this.y * scalar)
    operator fun div(scalar: Double) = Vec2d(this.x / scalar, this.y / scalar)
    operator fun unaryMinus() = Vec2d(-this.x, -this.y)
    infix fun dot(other: Vec2d) = this.x * other.x + this.y * other.y

    companion object {
        val ZERO = v(0.0, 0.0)
    }
}

fun v(x: Int, y: Int) = Vec2i(x, y)
fun v(x: Long, y: Long) = Vec2l(x, y)
fun v(x: Double, y: Double) = Vec2d(x, y)

operator fun <T> List2d<T>.get(x: Int, y: Int) = this[y][x]
operator fun <T> MutableList2d<T>.set(x: Int, y: Int, value: T) {
    this[y][x] = value
}

operator fun <T> List2d<T>.get(pos: Vec2i) = this[pos.y][pos.x]
operator fun <T> MutableList2d<T>.set(pos: Vec2i, value: T) {
    this[pos.y][pos.x] = value
}

enum class Direction4(val vec: Vec2i) {
    UP(v(0, -1)),
    RIGHT(v(1, 0)),
    DOWN(v(0, 1)),
    LEFT(v(-1, 0));

    fun turnLeft() = when (this) {
        UP -> LEFT
        LEFT -> DOWN
        DOWN -> RIGHT
        RIGHT -> UP
    }

    fun turnRight() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }

    companion object {
        @Deprecated("use entries", ReplaceWith("Direction4.entries"))
        val d4 = listOf(UP, RIGHT, DOWN, LEFT)
    }
}

enum class Direction8(val vec: Vec2i) {
    UP(v(0, -1)),
    UP_RIGHT(v(1, -1)),
    RIGHT(v(1, 0)),
    DOWN_RIGHT(v(1, 1)),
    DOWN(v(0, 1)),
    DOWN_LEFT(v(-1, 1)),
    LEFT(v(-1, 0)),
    UP_LEFT(v(-1, -1));

    companion object {
        val d8 = listOf(UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT)
    }
}