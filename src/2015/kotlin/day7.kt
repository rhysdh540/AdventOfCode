import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return mappings.third("a")
}

private fun PuzzleInput.part2(): Any? {
    val (_, resolved, get) = mappings
    val a = get("a")
    resolved.clear()
    resolved["b"] = a
    return get("a")
}

// 1. mappings: a map of output to function
// 2. resolved: a cache of output to value (so it doesn't take forever)
// 3. get: a function that takes an input and returns the value (this is what we use to get the value of "a")
private val PuzzleInput.mappings: Triple<
        MutableMap<String, () -> UShort>,
        MutableMap<String, UShort>,
            (String) -> UShort>
    get() {
        val mappings = mutableMapOf<String, () -> UShort>()
        val resolved = mutableMapOf<String, UShort>()

        fun get(input: String): UShort {
            if (resolved.contains(input)) return resolved[input]!!
            return if (mappings.contains(input)) {
                mappings[input]!!.invoke()
            } else {
                input.toUShort()
            }.also {
                resolved[input] = it
            }
        }

        lines.forEach {
            val (input, output) = it.split(" -> ")
            val insns = input.split(" ")
            when (insns.size) {
                1 -> mappings[output] = {
                    get(insns[0])
                }

                2 -> mappings[output] = {
                    if (insns[0] != "NOT") error("unexpected prefix operator ${insns[0]}")
                    get(insns[1]).inv()
                }

                3 -> mappings[output] = {
                    val op = insns[1]
                    val a = get(insns[0])
                    val b = get(insns[2])

                    when (op) {
                        "AND" -> a and b
                        "OR" -> a or b
                        "RSHIFT" -> a shr b
                        "LSHIFT" -> a shl b
                        else -> error("unknown operator $op")
                    }
                }
            }
        }

        return Triple(mappings, resolved, ::get)
    }

fun main() = PuzzleInput(2015, 7).withSolutions({ part1() }, { part2() }).run()