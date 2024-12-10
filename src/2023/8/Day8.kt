import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_8(input: String): Any? {
    val (instructions, allNodes) = input.split("\n\n").map { it.lines() }
    val insns = instructions[0].toCharArray().map { it == 'R' }.toBooleanArray()
    val nodes = allNodes.associate {
        val (a, b, c) = Regex("""(...) = \((...), (...)\)""").find(it)!!.destructured
        a to (b to c)
    }

    return generateSequence("AAA" to 0) { (it, count) ->
        if(it == "ZZZ") null
        else {
            val (left, right) = nodes[it]!!
            val next = if (insns[count % insns.size]) right else left
            next to count + 1
        }
    }.last().second
}

fun part2_8(input: String): Any? {
    val (instructions, allNodes) = input.split("\n\n").map { it.lines() }
    val insns = instructions[0].toCharArray().map { it == 'R' }.toBooleanArray()
    val nodes = allNodes.associate {
        val (a, b, c) = Regex("""(...) = \((...), (...)\)""").find(it)!!.destructured
        a to (b to c)
    }

    fun Long.gcd(other: Long): Long = if (other == 0L) this else other.gcd(this % other)
    infix fun Long.lcm(other: Long) = this * other / this.gcd(other)

    val periods = nodes.filter { it.key[2] == 'A' }.map {
        generateSequence(it.key to 0) { (it, count) ->
            if(it[2] == 'Z') null
            else {
                val (left, right) = nodes[it]!!
                val next = if (insns[count % insns.size]) right else left
                next to count + 1
            }
        }.last().second.toLong()
    }

    return periods.reduce { acc, i -> acc lcm i }
}

fun main() {
    val input = Path("inputs/2023/8.txt").readText()

    var start = System.nanoTime()
    var result = part1_8(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_8(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}