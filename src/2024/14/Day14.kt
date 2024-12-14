import kotlin.io.path.Path
import kotlin.io.path.readText
import Day14.Robot

fun part1_14(input: String): Any? {
    val robots = input.lines().map {
        val (x, y, dx, dy) = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""").find(it)!!.destructured
        Robot(x.toInt(), y.toInt(), dx.toInt(), dy.toInt())
    }

    repeat(100) {
        robots.forEach { it.move() }
    }

    val quadrants = IntArray(4)
    robots.forEach {
        if(it.x < 50) {
            if(it.y < 51) quadrants[0]++
            else if(it.y > 51) quadrants[2]++
        } else if(it.x > 50) {
            if(it.y < 51) quadrants[1]++
            else if(it.y > 51) quadrants[3]++
        }
    }

    return quadrants.reduce { acc, i -> acc * i }
}

fun part2_14(input: String): Int {
    val robots = input.lines().map {
        val (x, y, dx, dy) = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""").find(it)!!.destructured
        Robot(x.toInt(), y.toInt(), dx.toInt(), dy.toInt())
    }

    var i = 0
    while (true) {
        i++
        robots.forEach { it.move() }
        // The input generator likely started from a grid with no overlapping robots (when it started with the tree picture)
        // and then moved backwards to the initial state
        // So we can just check if there are no overlapping robots
        if(robots.all { r -> robots.count { it.x == r.x && it.y == r.y } == 1 }) {
            //uncomment to print the grid
//            Array(103) { x -> CharArray(101) { y -> if(robots.any { it.x == y && it.y == x }) '#' else '.' } }
//                .forEach { println(it.concatToString()) }
            return i
        }
    }
}

object Day14 {
    data class Robot(var x: Int, var y: Int, var dx: Int, var dy: Int) {
        fun move() {
            x += dx
            y += dy

            if (x < 0) x += 101
            if (x >= 101) x -= 101
            if (y < 0) y += 103
            if (y >= 103) y -= 103
        }
    }
}

fun main() {
    val input = Path("inputs/2024/14.txt").readText()

    var start = System.nanoTime()
    var result = part1_14(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_14(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}