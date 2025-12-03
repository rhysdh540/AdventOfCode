import dev.rdh.aoc.*
import kotlin.math.sqrt

private fun PuzzleInput.part1(): Any? {
    val (times, distances) = lines.map {
        it.substring(it.indexOf(':') + 1).split(' ').filter { it.isNotEmpty() }.ints
    }

    return (times zip distances).map { (time, distance) -> calculate(time.toLong(), distance.toLong()) }
        .fold(1L) { acc, i -> acc * i }
}

private fun PuzzleInput.part2(): Any? {
    val (time, distance) = lines.map {
        it.substring(it.indexOf(':') + 1).replace(" ", "").toLong()
    }

    return calculate(time, distance)
}

private fun calculate(time: Long, distance: Long): Long {
    val discriminant = sqrt((time * time - 4 * distance).toDouble())
    val root1 = ((time + discriminant) / 2).toLong()
    val root2 = ((time - discriminant) / 2).toLong()
    return root1 - root2
}

fun main() = PuzzleInput(2023, 6).withSolutions({ part1() }, { part2() }).run()