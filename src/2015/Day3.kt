import kotlin.io.path.Path
import kotlin.io.path.readText

typealias Point = Pair<Int, Int>

fun part1_3(input: String): Any? {
    var santa = origin
    val visited = mutableSetOf<Point>()
    for(c in input) {
        visited.add(santa)
        santa += move(c)
    }

    return visited.size
}

fun part2_3(input: String): Any? {
    var santa = origin
    var roboSanta = origin
    val visited = mutableSetOf<Point>()
    for((a, b) in input.chunked(2).map { it.toCharArray() }.filter { it.size == 2 }) {
        visited.add(santa)
        visited.add(roboSanta)
        santa += move(a)
        roboSanta += move(b)
    }

    return visited.size
}

operator fun Point.plus(other: Point): Point {
    return Point(first + other.first, second + other.second)
}

val origin = Point(0, 0)

fun move(c: Char): Point {
    return when(c) {
        'v' -> Point(0, 1)
        '>' -> Point(1, 0)
        '^' -> Point(0, -1)
        '<' -> Point(-1, 0)
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