import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_13(input: String): Any? {
    return Day13.run(input, false)
}

fun part2_13(input: String): Any? {
    return Day13.run(input, true)
}

object Day13 {
    fun run(input: String, fixSmudge: Boolean): Int {
        val grids = input.split("\n\n").map { it.lines() }
        return grids.sumOf {
            val found = findReflection(it, fixSmudge)
            if(found != 0) return@sumOf found * 100
            else findReflection(it.rotateClockwise(), fixSmudge)
        }
    }

    private fun findReflection(grid: List<String>, fixSmudge: Boolean): Int {
        for (r in 1..<grid.size) {
            val diff = grid.slice(0 until r).reversed().zip(
                grid.slice(r until grid.size)
            ).sumOf { (rowA, rowB) ->
                rowA.zip(rowB).count { (a, b) -> a != b }
            }

            if (diff == 0 || (fixSmudge && diff == 1)) {
                return r
            }
        }

        return 0
    }

    private fun Iterable<String>.rotateClockwise(): List<String> {
        val l = this.toList()
        val numRows = l.size
        val numCols = l[0].length
        val rotated = Array(numCols) { CharArray(numRows) }
        for (i in l.indices) {
            for (j in l[i].indices) {
                rotated[j][numRows - 1 - i] = l[i][j]
            }
        }
        return rotated.map { String(it) }
    }
}

fun main() {
    val input = Path("inputs/2023/13.txt").readText()

    var start = System.nanoTime()
    var result = part1_13(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_13(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}