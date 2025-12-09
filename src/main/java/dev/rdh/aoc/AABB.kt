package dev.rdh.aoc

class AABB(
    val minX: Int, val minY: Int, val minZ: Int,
    val maxX: Int, val maxY: Int, val maxZ: Int
) {
    constructor(min: Vec3i, max: Vec3i) : this(min.x, min.y, min.z, max.x, max.y, max.z)
    fun volume(): Long {
        return (maxX.toLong() - minX.toLong() + 1) *
               (maxY.toLong() - minY.toLong() + 1) *
               (maxZ.toLong() - minZ.toLong() + 1)
    }

    fun intersects(other: AABB): Boolean {
        return !(other.minX > maxX || other.maxX < minX ||
                 other.minY > maxY || other.maxY < minY ||
                 other.minZ > maxZ || other.maxZ < minZ)
    }

    val min: Vec3i by lazy { v(minX, minY, minZ) }
    val max: Vec3i by lazy { v(maxX, maxY, maxZ) }
}