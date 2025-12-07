import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val w = grid[0].indices
    val h = grid.indices

    val map = mutableMapOf<Char, List<Vec2i>>()
    for (i in h) {
        for (j in w) {
            if (grid[i][j] != '.') {
                map[grid[i][j]] = map.getOrDefault(grid[i][j], listOf()) + v(i, j)
            }
        }
    }

    val set = mutableSetOf<Vec2i>()
    for ((_, points) in map) {
        for (j in points.indices) {
            for (k in (j + 1)..<points.size) {
                val a = points[j]
                val b = points[k]

                val dx = b.x - a.x
                val dy = b.y - a.y

                set.add(v(a.x - dx, a.y - dy))
                set.add(v(b.x + dx, b.y + dy))
            }
        }
    }

    return set.filter { it.x in h && it.y in w }.size
}

private fun PuzzleInput.part2(): Any? {
    val w = grid[0].indices
    val h = grid.indices

    val map = mutableMapOf<Char, List<Vec2i>>()
    for (i in h) {
        for (j in w) {
            if (grid[i][j] != '.') {
                map[grid[i][j]] = map.getOrDefault(grid[i][j], listOf()) + v(i, j)
            }
        }
    }

    val set = mutableSetOf<Vec2i>()
    for ((_, points) in map) {
        for (j in points.indices) {
            for (k in (j + 1)..<points.size) {
                val a = points[j]
                val b = points[k]

                var dx = b.x - a.x
                var dy = b.y - a.y

                val gcd = dx gcd dy
                dx /= gcd
                dy /= gcd

                // x backwards
                var cx = a.x - dx
                var cy = a.y - dy
                while (cx in h && cy in w) {
                    set.add(v(cx, cy))
                    cx -= dx
                    cy -= dy
                }

                // y forwards
                cx = b.x + dx
                cy = b.y + dy
                while (cx in h && cy in w) {
                    set.add(v(cx, cy))
                    cx += dx
                    cy += dy
                }

                // between x and y
                cx = a.x
                cy = a.y
                repeat(gcd + 1) {
                    set.add(v(cx, cy))
                    cx += dx
                    cy += dy
                }
            }
        }
    }

    return set.size
}

fun main() = PuzzleInput(2024, 8).withSolutions({ part1() }, { part2() }).run()