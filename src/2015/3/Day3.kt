import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_3(input: String): Any? {
    var santa = origin
    val visited = mutableSetOf<Pair<Int, Int>>()
    for(c in input) {
        visited.add(santa)
        santa += move(c)
    }

    return visited.size
}

fun part2_3(input: String): Any? {
    var santa = origin
    var roboSanta = origin
    val visited = mutableSetOf<Pair<Int, Int>>()
    for(instructions in input.chunked(2)) {
        visited.add(santa)
        visited.add(roboSanta)
        santa += move(instructions[0])
        if(instructions.length > 1) {
            roboSanta += move(instructions[1])
        }
    }

    return visited.size
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(first + other.first, second + other.second)
}

val origin = Pair(0, 0)

fun move(c: Char): Pair<Int, Int> {
    return when(c) {
        'v' -> Pair(0, 1)
        '>' -> Pair(1, 0)
        '^' -> Pair(0, -1)
        '<' -> Pair(-1, 0)
        else -> throw AssertionError()
    }
}

fun main() {
    val input = Path("inputs/2015/3.txt").readText()

    var start = System.nanoTime()
    var result = part1_3(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_3(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}