import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return Regex("-?\\d+").findAll(input).map { it.value.toInt() }.sum()
}

private fun PuzzleInput.part2(): Any? {
    return sum(Json().parse<Map<String, Any>>(input)!!)
}

private fun sum(json: Any): Int {
    return when (json) {
        is Number -> json.toInt()
        is List<*> -> json.sumOf { sum(it!!) }
        is Map<*, *> -> {
            if (json.values.contains("red")) 0
            else json.values.sumOf { sum(it!!) }
        }

        else -> 0
    }
}

fun main() = PuzzleInput(2015, 12).withSolutions({ part1() }, { part2() }).run()