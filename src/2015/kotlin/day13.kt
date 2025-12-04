import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return run {}
}

private fun PuzzleInput.part2(): Any? {
    return run { happinesses ->
        happinesses["Me"] = mutableMapOf()
        happinesses.keys.forEach {
            if (it != "Me") {
                happinesses[it]!!["Me"] = 0
                happinesses["Me"]!![it] = 0
            }
        }
    }
}

private inline fun PuzzleInput.run(addMe: (MutableMap<String, MutableMap<String, Int>>) -> Unit): Any? {
    val happinesses = mutableMapOf<String, MutableMap<String, Int>>()
    val regex = Regex("""(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+).""")

    lines.forEach {
        val (person, gainOrLose, amount, neighbor) = regex.find(it)!!.destructured
        val value = if (gainOrLose == "gain") amount.toInt() else -amount.toInt()
        happinesses.getOrPut(person) { mutableMapOf() }[neighbor] = value
    }

    addMe(happinesses)

    return happinesses.keys.toList().permutations().maxOfOrNull { perm ->
        perm.windowed(2).sumOf { (a, b) ->
            happinesses[a]!![b]!! + happinesses[b]!![a]!!
        } + happinesses[perm.first()]!![perm.last()]!! + happinesses[perm.last()]!![perm.first()]!!
    }
}

fun main() = PuzzleInput(2015, 13).withSolutions({ part1() }, { part2() }).run()