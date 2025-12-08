package dev.rdh.aoc

import kotlin.math.absoluteValue

fun Vec2i.dist2(other: Vec2i): Long {
    val dx = (this.x - other.x).toLong()
    val dy = (this.y - other.y).toLong()
    return dx * dx + dy * dy
}

fun Vec2l.dist2(other: Vec2l): Long {
    val dx = this.x - other.x
    val dy = this.y - other.y
    return dx * dx + dy * dy
}

fun Vec2d.dist2(other: Vec2d): Double {
    val dx = this.x - other.x
    val dy = this.y - other.y
    return dx * dx + dy * dy
}

fun Vec3i.dist2(other: Vec3i): Long {
    val dx = (this.x - other.x).toLong()
    val dy = (this.y - other.y).toLong()
    val dz = (this.z - other.z).toLong()
    return dx * dx + dy * dy + dz * dz
}

fun Vec3l.dist2(other: Vec3l): Long {
    val dx = this.x - other.x
    val dy = this.y - other.y
    val dz = this.z - other.z
    return dx * dx + dy * dy + dz * dz
}

fun Vec3d.dist2(other: Vec3d): Double {
    val dx = this.x - other.x
    val dy = this.y - other.y
    val dz = this.z - other.z
    return dx * dx + dy * dy + dz * dz
}

fun Vec2i.manhattan(other: Vec2i): Int {
    return (this.x - other.x).absoluteValue +
        (this.y - other.y).absoluteValue
}

fun Vec2l.manhattan(other: Vec2l): Long {
    return (this.x - other.x).absoluteValue +
        (this.y - other.y).absoluteValue
}

fun Vec2d.manhattan(other: Vec2d): Double {
    return (this.x - other.x).absoluteValue +
        (this.y - other.y).absoluteValue
}

fun Vec3i.manhattan(other: Vec3i): Int {
    return (this.x - other.x).absoluteValue +
        (this.y - other.y).absoluteValue +
        (this.z - other.z).absoluteValue
}

fun Vec3l.manhattan(other: Vec3l): Long {
    return (this.x - other.x).absoluteValue +
        (this.y - other.y).absoluteValue +
        (this.z - other.z).absoluteValue
}

fun Vec3d.manhattan(other: Vec3d): Double {
    return (this.x - other.x).absoluteValue +
        (this.y - other.y).absoluteValue +
        (this.z - other.z).absoluteValue
}