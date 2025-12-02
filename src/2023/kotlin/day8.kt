import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (instructions, allNodes) = sections.map { it.lines() }
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

private fun PuzzleInput.part2(): Any? {
    val (instructions, allNodes) = sections.map { it.lines() }
    val insns = instructions[0].toCharArray().map { it == 'R' }.toBooleanArray()
    val nodes = allNodes.associate {
        val (a, b, c) = Regex("""(...) = \((...), (...)\)""").find(it)!!.destructured
        a to (b to c)
    }

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

    return periods.reduce(Long::lcm)
}

fun main() {
    val input = PuzzleInput(2023, 8)

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