import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_17(input: String): Any? {
    val containers = input.lines().map { it.toInt() }
    val target = 150

    // 1 << containers.size is the number of subsets because each container can either be included or not
    return (1 until (1 shl containers.size)).count { i ->
        target == containers.indices.sumOf { j ->
            // If the j-th bit is set, include the j-th container
            if (i and (1 shl j) != 0) {
                containers[j]
            } else {
                0
            }
        }
    }
}

fun part2_17(input: String): Any? {
    val containers = input.lines().map { it.toInt() }
    val target = 150

    val valid = (1 until (1 shl containers.size)).filter { i ->
        target == containers.indices.sumOf { j ->
            if (i and (1 shl j) != 0) {
                containers[j]
            } else {
                0
            }
        }
    }

    val min = valid.minOf { i ->
        containers.indices.count { j -> (i and (1 shl j) != 0) }
    }

    return valid.count { i ->
        containers.indices.count { j -> (i and (1 shl j) != 0) } == min
    }
}

fun main() {
    val input = Path("inputs/2015/17.txt").readText()

    var start = System.nanoTime()
    var result = part1_17(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_17(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}