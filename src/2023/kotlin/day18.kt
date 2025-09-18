import dev.rdh.aoc.*
import kotlin.math.abs

private fun PuzzleInput.part1(): Any? {
    val insns = lines.map {
        val (dir, num, _) = it.split(' ')
        dir to num.toLong()
    }

    return run(insns)
}

@OptIn(ExperimentalStdlibApi::class)
private fun PuzzleInput.part2(): Any? {
    val insns = lines.map {
        val insn = it.split(' ')[2].removePrefix("(#").removeSuffix(")")
        val num = insn.dropLast(1).hexToLong()
        val dir = mapOf(
            '0' to "R",
            '1' to "D",
            '2' to "L",
            '3' to "U"
        )[insn.last()]!!
        dir to num
    }

    return run(insns)
}

private fun run(insns: List<Pair<String, Long>>): Long {
    var pos = 0L to 0L
    val vertices = mutableListOf<Pair<Long, Long>>()
    vertices += pos

    var boundary = 0L // number of points on the perimeter
    for ((dir, num) in insns) {
        val d = when (dir) {
            "U" -> 0L to 1L
            "D" -> 0L to -1L
            "L" -> -1L to 0L
            "R" -> 1L to 0L
            else -> error("Unknown direction: $dir")
        }

        pos += d * num
        vertices += pos
        boundary += num
    }

    require(pos == Pair(0L, 0L)) { "Path must return to origin, found at $pos" }

    // shoelace
    val area = vertices.zipWithNext().sumOf { (p1, p2) ->
        val (x1, y1) = p1
        val (x2, y2) = p2
        x1 * y2 - x2 * y1
    }.let { abs(it) / 2L }

    // reverse pick's theorem to find number of interior points
    // A = I + B/2 - 1
    // rearranged:
    // I = A - B/2 + 1
    return area + boundary / 2 + 1
}

fun main() {
    val input = getInput(2023, 18)

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