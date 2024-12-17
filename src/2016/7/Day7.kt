import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_7(input: String): Any? {
    return input.lines().count {
        val parts = it.split("[", "]")
        val hypernet = parts.filterIndexed { index, _ -> index % 2 == 1 }
        val supernet = parts.filterIndexed { index, _ -> index % 2 == 0 }

        val abba = supernet.any { s ->
            s.windowed(4).any { it[0] == it[3] && it[1] == it[2] && it[0] != it[1] }
        }

        val noAbba = hypernet.none { s ->
            s.windowed(4).any { it[0] == it[3] && it[1] == it[2] && it[0] != it[1] }
        }

        abba && noAbba
    }
}

fun part2_7(input: String): Any? {
    return input.lines().count {
        val parts = it.split("[", "]")
        val hypernet = parts.filterIndexed { index, _ -> index % 2 == 1 }
        val supernet = parts.filterIndexed { index, _ -> index % 2 == 0 }

        val aba = supernet.flatMap { s ->
            s.windowed(3).filter { it[0] == it[2] && it[0] != it[1] }
        }

        val bab = aba.map { "${it[1]}${it[0]}${it[1]}" }

        val babInHypernet = hypernet.any { h ->
            bab.any { h.contains(it) }
        }

        babInHypernet
    }
}

fun main() {
    val input = Path("inputs/2016/7.txt").readText()

    var start = System.nanoTime()
    var result = part1_7(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_7(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}