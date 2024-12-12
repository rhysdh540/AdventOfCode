import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_12(input: String): Any? {
    val grid = input.split("\n").map { it.toCharArray() }
    val visited = mutableSetOf<Pair<Int, Int>>()

    fun ff(x: Int, y: Int, char: Char): Set<Pair<Int, Int>> {
        val blob = mutableSetOf<Pair<Int, Int>>()
        val q = ArrayDeque<Pair<Int, Int>>()
        q.add(Pair(x, y))
        visited.add(Pair(x, y))

        while (q.isNotEmpty()) {
            val (cx, cy) = q.removeFirst()
            blob.add(Pair(cx, cy))

            val neigbors = listOf(
                Pair(cx + 1, cy),
                Pair(cx - 1, cy),
                Pair(cx, cy + 1),
                Pair(cx, cy - 1)
            )

            for ((nx, ny) in neigbors) {
                if (nx in grid[0].indices && ny in grid.indices &&
                    grid[ny][nx] == char &&
                    Pair(nx, ny) !in visited
                ) {
                    visited.add(Pair(nx, ny))
                    q.add(Pair(nx, ny))
                }
            }
        }

        return blob
    }

    val blobs = mutableListOf<Set<Pair<Int, Int>>>()

    for (y in grid.indices) {
        for (x in grid[y].indices) {
            if (Pair(x, y) !in visited) {
                val char = grid[y][x]
                blobs.add(ff(x, y, char))
            }
        }
    }

    return blobs.sumOf { b ->
        b.size * b.sumOf { (x, y) ->
            listOf(
                Pair(x + 1, y),
                Pair(x - 1, y),
                Pair(x, y + 1),
                Pair(x, y - 1)
            ).count { it !in b }
        }
    }
}

fun part2_12(input: String): Any? {
    val grid = input.split("\n").map { it.toCharArray() }
    val visited = mutableSetOf<Pair<Int, Int>>()

    fun ff(x: Int, y: Int, char: Char): Set<Pair<Int, Int>> {
        val blob = mutableSetOf<Pair<Int, Int>>()
        val q = ArrayDeque<Pair<Int, Int>>()
        q.add(Pair(x, y))
        visited.add(Pair(x, y))

        while (q.isNotEmpty()) {
            val (cx, cy) = q.removeFirst()
            blob.add(Pair(cx, cy))

            val neigbors = listOf(
                Pair(cx + 1, cy),
                Pair(cx - 1, cy),
                Pair(cx, cy + 1),
                Pair(cx, cy - 1)
            )

            for ((nx, ny) in neigbors) {
                if (nx in grid[0].indices && ny in grid.indices &&
                    grid[ny][nx] == char &&
                    Pair(nx, ny) !in visited
                ) {
                    visited.add(Pair(nx, ny))
                    q.add(Pair(nx, ny))
                }
            }
        }

        return blob
    }

    fun calculatePerimeter(blob: Set<Pair<Int, Int>>): Int {
        val vert = mutableSetOf<Triple<Int, Int, Int>>()
        val hor = mutableSetOf<Triple<Int, Int, Int>>()

        for ((x, y) in blob) {
            if (Pair(x - 1, y) !in blob) vert.add(Triple(x, y, -1))
            if (Pair(x + 1, y) !in blob) vert.add(Triple(x + 1, y, 1))
            if (Pair(x, y - 1) !in blob) hor.add(Triple(x, y, -1))
            if (Pair(x, y + 1) !in blob) hor.add(Triple(x, y + 1, 1))
        }

        fun calculateGroupedEdges(edges: Set<Triple<Int, Int, Int>>, isVert: Boolean): Int {
            val groupedEdges = edges.groupBy {
                Pair(if (isVert) it.first else it.second, it.third)
            }

            return groupedEdges.values.sumOf { group ->
                val sortedEdges = if (isVert) group.sortedBy { it.second } else group.sortedBy { it.first }

                1 + sortedEdges.zipWithNext().count { (a, b) ->
                    if (isVert) {
                        b.second != a.second + 1
                    } else {
                        b.first != a.first + 1
                    }
                }
            }
        }

        return calculateGroupedEdges(vert, true) + calculateGroupedEdges(hor, false)
    }

    val blobs = mutableListOf<Set<Pair<Int, Int>>>()

    for (y in grid.indices) {
        for (x in grid[y].indices) {
            if (Pair(x, y) !in visited) {
                blobs.add(ff(x, y, grid[y][x]))
            }
        }
    }

    return blobs.sumOf { it.size * calculatePerimeter(it.toSet()) }
}


fun main() {
    val input = Path("inputs/2024/12.txt").readText()

    var start = System.nanoTime()
    var result = part1_12(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_12(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}