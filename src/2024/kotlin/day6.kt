import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return walk(parseMap()).size + 1
}

private fun PuzzleInput.part2(): Any? {
    val map = parseMap()

    val jumps = JumpMap(map)
    val seen = IntArray(map.width * map.height shl 2) { -1 }

    return walk(map).withIndex().count { (generation, and) ->
        jumps.loops(map.startIndex, and, seen, generation)
    }
}

private data class Map(
    val width: Int,
    val height: Int,
    val walls: BooleanArray,
    val startIndex: Int,
)

private fun PuzzleInput.parseMap(): Map {
    val raw = grid
    val height = raw.size
    val width = raw[0].size

    val walls = BooleanArray(width * height)
    var startIndex: Int? = null

    for (r in 0..<height) {
        val row = raw[r]
        for (c in 0..<width) {
            val ch = row[c]
            val idx = r * width + c
            when (ch) {
                '#' -> walls[idx] = true
                '^' -> startIndex = idx
            }
        }
    }

    return Map(width, height, walls, startIndex ?: error("No start position found"))
}

private fun walk(map: Map): IntArray {
    val (width, height, walls, startIndex) = map
    val total = width * height

    val visited = BooleanArray(total)
    val order = IntArray(total)
    var orderSize = 0

    var row = startIndex / width
    var col = startIndex % width
    var dir = 0 // 0 = up, 1 = right, 2 = down, 3 = left

    visited[startIndex] = true

    while (true) {
        val (nc, nr) = v(col, row) + Vectors.d4[dir]
        if (nr !in 0..<height || nc !in 0..<width) {
            break
        }

        val nextIndex = nr * width + nc
        if (walls[nextIndex]) {
            dir = (dir + 1) and 3
            continue
        }

        row = nr
        col = nc

        if (!visited[nextIndex]) {
            visited[nextIndex] = true
            order[orderSize++] = nextIndex
        }
    }

    var count = 0
    for (i in 0..<total) {
        if (visited[i]) count++
    }

    return order.copyOf(orderSize)
}

private class JumpMap(map: Map) {
    private val up: IntArray
    private val down: IntArray
    private val left: IntArray
    private val right: IntArray

    private val w = map.width
    private val h = map.height

    init {
        val walls = map.walls
        val total = w * h
        up = IntArray(total)
        down = IntArray(total)
        left = IntArray(total)
        right = IntArray(total)

        // vertical jumps
        for (c in 0..<w) {
            // go top->bottom
            var last = -1
            for (r in 0..<h) {
                val idx = r * w + c
                if (walls[idx]) {
                    last = r + 1
                }
                up[idx] = last
            }

            // go bottom->top
            last = h
            for (r in h - 1 downTo 0) {
                val idx = r * w + c
                if (walls[idx]) {
                    last = r - 1
                }
                down[idx] = last
            }
        }

        // horizontal jumps
        for (r in 0..<h) {
            // go left->right
            var last = -1
            for (c in 0..<w) {
                val idx = r * w + c
                if (walls[idx]) {
                    last = c + 1
                }
                left[idx] = last
            }

            // go right->left
            last = w
            for (c in w - 1 downTo 0) {
                val idx = r * w + c
                if (walls[idx]) {
                    last = c - 1
                }
                right[idx] = last
            }
        }
    }

    fun loops(
        startIndex: Int,
        obstacleIndex: Int,
        seen: IntArray,
        generation: Int,
    ): Boolean {
        val obstacleRow = obstacleIndex / w
        val obstacleCol = obstacleIndex % w

        var row = startIndex / w
        var col = startIndex % w
        var dir = 0

        while (row in 0..<h && col in 0..<w) {
            val stateIndex = ((row * w + col) shl 2) or dir
            if (seen[stateIndex] == generation) {
                return true
            }

            seen[stateIndex] = generation

            val idx = row * w + col
            when (dir) {
                0, 2 -> { // vertical
                    val u = dir == 0
                    var destRow: Int
                    val range: IntRange
                    if (u) {
                        destRow = up[idx]
                        range = destRow..<row
                    } else {
                        destRow = down[idx]
                        range = (row + 1)..destRow
                    }

                    if (obstacleCol == col && obstacleRow in range) {
                        destRow = if (u) obstacleRow + 1 else obstacleRow - 1
                    }

                    if (destRow !in 0..<h) {
                        return false
                    }
                    row = destRow
                }
                3, 1 -> { // horizontal
                    val l = dir == 3
                    var destCol: Int
                    val range: IntRange
                    if (l) {
                        destCol = left[idx]
                        range = destCol..<col
                    } else {
                        destCol = right[idx]
                        range = (col + 1)..destCol
                    }
                    if (obstacleRow == row && obstacleCol in range) {
                        destCol = if (l) obstacleCol + 1 else obstacleCol - 1
                    }

                    if (destCol !in 0..<w) {
                        return false
                    }
                    col = destCol
                }
            }

            dir = (dir + 1) and 3
        }

        return false
    }
}

fun main() = PuzzleInput(2024, 6).withSolutions({ part1() }, { part2() }).run()