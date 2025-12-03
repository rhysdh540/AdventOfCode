import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (instructions, allNodes) = sections.map { it.lines() }
    val insns = instructions[0].toCharArray().map { it == 'R' }.toBooleanArray()
    val nodes = allNodes.associate {
        val (a, b, c) = Regex("""(...) = \((...), (...)\)""").find(it)!!.destructured
        a to (b to c)
    }

    return generateSequence("AAA" to 0) { (it, count) ->
        if (it == "ZZZ") null
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
            if (it[2] == 'Z') null
            else {
                val (left, right) = nodes[it]!!
                val next = if (insns[count % insns.size]) right else left
                next to count + 1
            }
        }.last().second.toLong()
    }

    return periods.reduce(Long::lcm)
}

fun main() = PuzzleInput(2023, 8).withSolutions({ part1() }, { part2() }).run()