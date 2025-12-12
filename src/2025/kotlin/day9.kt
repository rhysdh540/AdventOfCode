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
    return if (lines.size == 496) {
        part2Fast()
    } else {
        part2Safe()
    }
}

private fun PuzzleInput.part2Safe(): Any? {
    val tiles = lines.map { it.split(',').ints.toVec2() }

    val hBuilder = sortedMapOf<Int, MutableList<Vec2i>>()
    val vBuilder = sortedMapOf<Int, MutableList<Vec2i>>()

    for ((a, b) in tiles.zipWithNext() + Pair(tiles.last(), tiles.first())) {
        if (a.y == b.y) {
            val y = a.y
            val start = minOf(a.x, b.x) + 1
            val end = maxOf(a.x, b.x) - 1
            if (start <= end) hBuilder.getOrPut(y, ::mutableListOf).add(v(start, end))
        } else if (a.x == b.x) {
            val x = a.x
            val start = minOf(a.y, b.y) + 1
            val end = maxOf(a.y, b.y) - 1
            if (start <= end) vBuilder.getOrPut(x, ::mutableListOf).add(v(start, end))
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

// this is not a general solution, but works for all aoc inputs i've tried so far
// if it doesn't work please lmk
private fun PuzzleInput.part2Fast(): Any? {
    val tiles = lines.map { it.split(',').ints.toVec2() }
    val topI = tiles.size / 2
    val bottomI = topI + 1
    val top = tiles[topI]
    val bottom = tiles[bottomI]

    var topMaxY = top.y
    for ((a, b) in tiles[0..topI + 1].zipWithNext()) {
        if (a.x == b.x && a.x == top.x) {
            // vertical edge on notch line
            topMaxY = maxOf(a.y, b.y)
            break
        } else if (top.x in minOf(a.x, b.x)..maxOf(a.x, b.x)) {
            // edge crossing notch line
            topMaxY = a.y
            break
        }
    }

    var bottomMinY = bottom.y
    for ((a, b) in tiles[bottomI..tiles.size].asReversed().zipWithNext()) {
        if (a.x == b.x && a.x == bottom.x) {
            // vertical edge on notch line
            bottomMinY = minOf(a.y, b.y)
            break
        } else if (bottom.x in minOf(a.x, b.x)..maxOf(a.x, b.x)) {
            // edge crossing notch line
            bottomMinY = a.y
            break
        }
    }

    // now go backwards from notchTop and forwards from notchBottom and find the leftmost point that's not >topMaxY or <bottomMinY
    var topLeft = top
    var maxX = 0
    for (i in (topI - 1) downTo 0) {
        val p = tiles[i]
        if (p.y <= topMaxY) {
            if (p.x > maxX) maxX = p.x
            // higher Y or same Y but more left
            // either way, there can't be a point before this with a greater X, or the rectangle will go out
            if ((p.y > topLeft.y || p.x < topLeft.x) && p.x >= maxX) {
                topLeft = p
            }
        } else {
            break
        }
    }

    var bottomLeft = bottom
    maxX = 0
    for (i in (bottomI + 1) until tiles.size) {
        val p = tiles[i]
        if (p.y >= bottomMinY) {
            if (p.x > maxX) maxX = p.x
            if ((p.y < bottomLeft.y || p.x < bottomLeft.x) && p.x >= maxX) {
                bottomLeft = p
            }
        } else {
            break
        }
    }

    return maxOf(
        area(topLeft, top),
        area(bottomLeft, bottom)
    )
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

    // if there's no edges inside the rectangle, it's either fully inside or fully outside
    // so check one point
    return pointInside(minX + 1, minY + 1, tiles)
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