import dev.rdh.aoc.*
import java.util.SortedMap
import kotlin.math.abs

private fun PuzzleInput.part1(): Any? {
    val tiles = lines.map { it.split(',').ints.toVec2() }
    return tiles.flatMapIndexed { i, p1 ->
        tiles.asSequence().drop(i + 1).map { p2 ->
            area(p1, p2)
        }
    }.max()
}

private fun PuzzleInput.part2(): Any? {
    val tiles = lines.map { it.split(',').ints.toVec2() }

    val hBuilder = sortedMapOf<Int, MutableList<Vec2i>>()
    val vBuilder = sortedMapOf<Int, MutableList<Vec2i>>()

    for ((a, b) in tiles.zipWithNext() + Pair(tiles.last(), tiles.first())) {
        if (a.y == b.y) {
            val y = a.y
            val start = minOf(a.x, b.x) + 1
            val end = maxOf(a.x, b.x) - 1
            if (start <= end) hBuilder.computeIfAbsent(y) { mutableListOf() }.add(v(start, end))
        } else if (a.x == b.x) {
            val x = a.x
            val start = minOf(a.y, b.y) + 1
            val end = maxOf(a.y, b.y) - 1
            if (start <= end) vBuilder.computeIfAbsent(x) { mutableListOf() }.add(v(start, end))
        } else {
            error("Non-axis-aligned edge from $a to $b")
        }
    }

    val (h, v) = Intervals(hBuilder) to Intervals(vBuilder)

    var max = 0L
    for (i in tiles.indices) {
        val p1 = tiles[i]
        for (p2 in tiles.asSequence().drop(i + 1)) {
            val area = area(p1, p2)
            if (area > max && rectInside(p1, p2, tiles, h, v)) { // only check if we have to
                max = area
            }
        }
    }
    return max
}

private fun area(p1: Vec2i, p2: Vec2i): Long {
    return (abs(p2.x - p1.x).toLong() + 1) * (abs(p2.y - p1.y).toLong() + 1)
}

private class Intervals(
    private val coords: IntArray,
    private val intervals: Array<Array<Vec2i>>
) {
    constructor(m: SortedMap<Int, MutableList<Vec2i>>) : this(
        m.keys.toIntArray(),
        m.values.map { it.toTypedArray() }.toTypedArray()
    )

    fun blocks(minCoord: Int, maxCoord: Int, minOther: Int, maxOther: Int): Boolean {
        if (minCoord > maxCoord) return false
        // find first index with coord >= minCoord
        var from = coords.binarySearch(minCoord)
        if (from < 0) from = -from - 1

        // find first index with coord > maxCoord
        var to = coords.binarySearch(maxCoord + 1)
        if (to < 0) to = -to - 1

        for (i in from until to) {
            for ((s, e) in intervals[i]) {
                if (s <= maxOther && e >= minOther) return true
            }
        }
        return false
    }
}

private fun rectInside(p1: Vec2i, p2: Vec2i, tiles: List<Vec2i>, h: Intervals, v: Intervals): Boolean {
    val minX = minOf(p1.x, p2.x)
    val maxX = maxOf(p1.x, p2.x)
    val minY = minOf(p1.y, p2.y)
    val maxY = maxOf(p1.y, p2.y)

    // if one of polygon edges cuts through the rectangle, then one side of that edge is outside
    if (h.blocks(minY + 1, maxY - 1, minX, maxX) || v.blocks(minX + 1, maxX - 1, minY, maxY)) return false

    // if one of the corners is outside the polygon, then obviously the rectangle is not fully inside
    for (corner in listOf(v(minX, minY), v(minX, maxY), v(maxX, minY), v(maxX, maxY))) {
        if (!pointInside(corner.x, corner.y, tiles)) return false
    }

    return true
}

private fun pointInside(x: Int, y: Int, polygon: List<Vec2i>): Boolean {
    var inside = false
    var j = polygon.lastIndex

    for (i in polygon.indices) {
        val a = polygon[i]
        val b = polygon[j]

        // if our point is exactly on a polygon edge, consider it inside
        if (x in minOf(a.x, b.x)..maxOf(a.x, b.x) && y in minOf(a.y, b.y)..maxOf(a.y, b.y)) {
            return true
        }

        // shoot in imaginary ray to the right, if it crosses a..b, toggle inside/outside
        if ((a.y > y) != (b.y > y) && x < a.x + (b.x - a.x) * (y - a.y) / (b.y - a.y)) {
            inside = !inside
        }

        j = i
    }

    return inside
}

fun main() = PuzzleInput(2025, 9).withSolutions({ part1() }, { part2() }).run()