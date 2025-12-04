import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val w = grid[0].indices
    val h = grid.indices

    val map = mutableMapOf<Char, List<Pair<Int, Int>>>()
    for (i in h) {
        for (j in w) {
            if (grid[i][j] != '.') {
                map[grid[i][j]] = map.getOrDefault(grid[i][j], listOf()) + Pair(i, j)
            }
        }
    }

    val set = mutableSetOf<Pair<Int, Int>>()
    for ((_, points) in map) {
        for (j in points.indices) {
            for (k in (j + 1)..<points.size) {
                val a = points[j]
                val b = points[k]

                val dx = b.x - a.x
                val dy = b.y - a.y

                set.add(Pair(a.x - dx, a.y - dy))
                set.add(Pair(b.x + dx, b.y + dy))
            }
        }
    }

    return set.filter { it.x in h && it.y in w }.size
}

private fun PuzzleInput.part2(): Any? {
    val w = grid[0].indices
    val h = grid.indices

    val map = mutableMapOf<Char, List<Pair<Int, Int>>>()
    for (i in h) {
        for (j in w) {
            if (grid[i][j] != '.') {
                map[grid[i][j]] = map.getOrDefault(grid[i][j], listOf()) + Pair(i, j)
            }
        }
    }

    val set = mutableSetOf<Pair<Int, Int>>()
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
                    set.add(Pair(cx, cy))
                    cx -= dx
                    cy -= dy
                }

                // y forwards
                cx = b.x + dx
                cy = b.y + dy
                while (cx in h && cy in w) {
                    set.add(Pair(cx, cy))
                    cx += dx
                    cy += dy
                }

                // between x and y
                cx = a.x
                cy = a.y
                repeat(gcd + 1) {
                    set.add(Pair(cx, cy))
                    cx += dx
                    cy += dy
                }
            }
        }
    }

    return set.size
}

fun main() = PuzzleInput(2024, 8).withSolutions({ part1() }, { part2() }).run()