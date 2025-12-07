import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val visited = mutableSetOf<Vec2i>()

    fun ff(x: Int, y: Int, char: Char): Set<Vec2i> {
        val blob = mutableSetOf<Vec2i>()
        val q = ArrayDeque<Vec2i>()
        q.add(v(x, y))
        visited.add(v(x, y))

        while (q.isNotEmpty()) {
            val (cx, cy) = q.removeFirst()
            blob.add(v(cx, cy))

            val neigbors = listOf(
                v(cx + 1, cy),
                v(cx - 1, cy),
                v(cx, cy + 1),
                v(cx, cy - 1)
            )

            for (n in neigbors) {
                if (n.x in grid[0].indices && n.y in grid.indices &&
                    grid[n.y][n.x] == char &&
                    n !in visited
                ) {
                    visited.add(n)
                    q.add(n)
                }
            }
        }

        return blob
    }

    val blobs = grid.indices.flatMap { y ->
        grid[y].indices.mapNotNull { x ->
            if (v(x, y) !in visited) {
                ff(x, y, grid[y][x])
            } else null
        }
    }

    return blobs.sumOf { b ->
        b.size * b.sumOf { (x, y) ->
            listOf(
                v(x + 1, y),
                v(x - 1, y),
                v(x, y + 1),
                v(x, y - 1)
            ).count { it !in b }
        }
    }
}

private fun PuzzleInput.part2(): Any? {
    val visited = mutableSetOf<Vec2i>()

    fun ff(x: Int, y: Int, char: Char): Set<Vec2i> {
        val blob = mutableSetOf<Vec2i>()
        val q = ArrayDeque<Vec2i>()
        q.add(v(x, y))
        visited.add(v(x, y))

        while (q.isNotEmpty()) {
            val (cx, cy) = q.removeFirst()
            blob.add(v(cx, cy))

            val neigbors = listOf(
                v(cx + 1, cy),
                v(cx - 1, cy),
                v(cx, cy + 1),
                v(cx, cy - 1)
            )

            for (n in neigbors) {
                if (n.x in grid[0].indices && n.y in grid.indices &&
                    grid[n.y][n.x] == char &&
                    n !in visited
                ) {
                    visited.add(n)
                    q.add(n)
                }
            }
        }

        return blob
    }

    fun calculatePerimeter(blob: Set<Vec2i>): Int {
        val vert = mutableSetOf<Triple<Int, Int, Int>>()
        val hor = mutableSetOf<Triple<Int, Int, Int>>()

        for ((x, y) in blob) {
            if (v(x - 1, y) !in blob) vert.add(Triple(x, y, -1))
            if (v(x + 1, y) !in blob) vert.add(Triple(x + 1, y, 1))
            if (v(x, y - 1) !in blob) hor.add(Triple(x, y, -1))
            if (v(x, y + 1) !in blob) hor.add(Triple(x, y + 1, 1))
        }

        fun calculateGroupedEdges(edges: Set<Triple<Int, Int, Int>>, isVert: Boolean): Int {
            val groupedEdges = edges.groupBy {
                v(if (isVert) it.first else it.second, it.third)
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
            if (v(x, y) !in visited) {
                ff(x, y, grid[y][x])
            } else null
        }
    }

    return blobs.sumOf { it.size * calculatePerimeter(it.toSet()) }
}

fun main() = PuzzleInput(2024, 12).withSolutions({ part1() }, { part2() }).run()