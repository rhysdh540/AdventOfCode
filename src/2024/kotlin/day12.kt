import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
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

    val blobs = grid.indices.flatMap { y ->
        grid[y].indices.mapNotNull { x ->
            if (Pair(x, y) !in visited) {
                ff(x, y, grid[y][x])
            } else null
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

private fun PuzzleInput.part2(): Any? {
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

    val blobs = grid.indices.flatMap { y ->
        grid[y].indices.mapNotNull { x ->
            if (Pair(x, y) !in visited) {
                ff(x, y, grid[y][x])
            } else null
        }
    }

    return blobs.sumOf { it.size * calculatePerimeter(it.toSet()) }
}

fun main() {
    val input = getInput(2024, 12)

    var start = System.nanoTime()
    var result = input.part1()
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = input.part2()
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}