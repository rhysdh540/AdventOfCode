import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_25(input: String): Any? {
    val locks = input.split("\n\n").filter {
        it.lines().first().all { it == '#' }
    }.map {
        val skip = it.substringAfter("\n")
        skip.rotateRight().lines().map { it.count { it == '#' } }
    }

    val keys = input.split("\n\n").filter {
        it.lines().first().all { it == '.' }
    }.map {
        val skip = it.substringBeforeLast("\n")
        skip.rotateRight().lines().map { it.count { it == '#' } }
    }

    val locksToKeys = locks.flatMap { l ->
        keys.map { k -> l to k }
    }

    return locksToKeys.count { (l, k) ->
        l.zip(k).all { it.first + it.second <= 5 }
    }
}

fun part2_25(input: String): Any? {
    return "Merry Christmas!"
}

fun String.rotateRight(): String {
    val lines = this.lines()
    return (0 until lines.first().length).joinToString("\n") { i ->
        lines.reversed().map { it[i] }.joinToString("")
    }
}

fun main() {
    val input = Path("inputs/2024/25.txt").readText()

    var start = System.nanoTime()
    var result = part1_25(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_25(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}