import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_21(input: String): Any? {
    return Day21.run(input, 2)
}

fun part2_21(input: String): Any? {
    return Day21.run(input, 25)
}

object Day21 {
    fun run(input: String, num: Int): Long {
        return input.lines().sumOf {
            val len = makePath(it, num, true)
            val numericPart = it.dropLast(1).toInt()
            numericPart * len
        }
    }

    val keyPaths = findShortestPaths(
        "789",
        "456",
        "123",
        " 0A"
    )
    val dirPaths = findShortestPaths(
        " ^A",
        "<v>"
    )

    val makePathCache = mutableMapOf<Pair<String, Int>, Long>()

    fun makePath(combo: String, levels: Int, numpad: Boolean): Long {
        val key = Pair(combo, levels)
        makePathCache[key]?.let { return it }
        val paths = if (numpad) keyPaths else dirPaths

        // add A to the front since that's where you start
        val e = "A$combo".zipWithNext().sumOf { (start, end) ->
            val path = paths[start]!![end]!!
            if (levels == 0) {
                return@sumOf path.length.toLong() + 1
            } else {
                // add A to the path because you have to click A after each move
                return@sumOf makePath("${path}A", levels - 1, false)
            }
        }
        makePathCache[key] = e
        return e
    }

    // map returned maps a button to a map of buttons and the path to get there
    fun findShortestPaths(vararg buttons: String): Map<Char, Map<Char, String>> {
        val paths = mutableMapOf<Char, MutableMap<Char, String>>()

        // Map each button to its (row, column) position
        val buttonPositions = mutableMapOf<Char, Pair<Int, Int>>()
        for (i in buttons.indices) {
            for (j in buttons[i].indices) {
                val button = buttons[i][j]
                if (button != ' ') {
                    buttonPositions[button] = Pair(i, j)
                }
            }
        }

        for ((button, startPos) in buttonPositions) {
            for ((button1, endPos) in buttonPositions) {
                val path = findPath(buttons, startPos, endPos)
                paths.computeIfAbsent(button) { mutableMapOf() }[button1] = path
            }
        }

        return paths
    }

    // Find the shortest path between two buttons on a grid of buttons
    // also taking into account how "compressible" it will be when it's run through makePath
    fun findPath(
        buttons: Array<out String>,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>
    ): String {
        val commands = StringBuilder()

        val (startRow, startCol) = start
        val (endRow, endCol) = end
        val (minRow, maxRow) = minOf(startRow, endRow) to maxOf(startRow, endRow)
        val (minCol, maxCol) = minOf(startCol, endCol) to maxOf(startCol, endCol)

        fun moveVertical() {
            val rowDiff = endRow - startRow
            when {
                rowDiff > 0 -> commands.append("v".repeat(rowDiff))
                rowDiff < 0 -> commands.append("^".repeat(-rowDiff))
            }
        }

        fun moveHorizontal() {
            val colDiff = endCol - startCol
            when {
                colDiff > 0 -> commands.append(">".repeat(colDiff))
                colDiff < 0 -> commands.append("<".repeat(-colDiff))
            }
        }

        if (endCol > startCol) {
            // NE or SE
            // prefer vh
            // check that there's no space in endRow or startCol

            val spaceInEndRow = buttons[endRow].substring(minCol, maxCol).contains(' ')
            val spaceInStartCol = (minRow until maxRow).any { buttons[it][startCol] == ' ' }

            if (spaceInEndRow || spaceInStartCol) {
                // forced to go the othe way to avoid the space
                moveHorizontal()
                moveVertical()
            } else {
                // go preferred way
                moveVertical()
                moveHorizontal()
            }
        } else {
            // NW or SW
            // prefer hv
            // check that there's no space in endCol or startRow

            val spaceInEndCol = (minRow until maxRow).any { buttons[it][endCol] == ' ' }
            val spaceInStartRow = buttons[startRow].substring(minCol, maxCol).contains(' ')

            if (spaceInEndCol || spaceInStartRow) {
                // forced to go the othe way to avoid the space
                moveVertical()
                moveHorizontal()
            } else {
                // go preferred way
                moveHorizontal()
                moveVertical()
            }
        }

        return commands.toString()
    }
}

fun main() {
    val input = Path("inputs/2024/21.txt").readText()

    var start = System.nanoTime()
    var result = part1_21(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_21(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}