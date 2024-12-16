import kotlin.UShort
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_7(input: String): Any? {
    return Day7.getMappings(input).third.invoke("a")
}

fun part2_7(input: String): Any? {
    val (_, resolved, get) = Day7.getMappings(input)
    val a = get("a")
    resolved.clear()
    resolved["b"] = a
    return get("a")
}

object Day7 {
    // returns 3 things:
    // 1. mappings: a map of output to function
    // 2. resolved: a cache of output to value (so it doesn't take forever)
    // 3. get: a function that takes an input and returns the value (this is what we use to get the value of "a")
    fun getMappings(input: String): Triple<MutableMap<String, () -> UShort>, MutableMap<String, UShort>, (String) -> UShort> {
        val mappings = mutableMapOf<String, () -> UShort>()
        val resolved = mutableMapOf<String, UShort>()

        fun get(input: String): UShort {
            if (resolved.contains(input)) return resolved[input]!!
            val result = if (mappings.contains(input))
                mappings[input]!!.invoke()
            else
                input.toUShortOrNull() ?: error("unknown input $input")
            resolved[input] = result
            return result
        }

        input.lines().forEach {
            val (input, output) = it.split(" -> ")
            val insns = input.split(" ")
            when (insns.size) {
                1 -> mappings.put(output) {
                    get(insns[0])
                }

                2 -> mappings.put(output) {
                    if (insns[0] != "NOT") error("unexpected prefix operator ${insns[0]}")
                    get(insns[1]).inv()
                }

                3 -> mappings.put(output) {
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
}

infix fun UShort.shr(other: UShort): UShort {
    return (this.toInt() shr other.toInt()).toUShort()
}

infix fun UShort.shl(other: UShort): UShort {
    return (this.toInt() shl other.toInt()).toUShort()
}

fun main() {
    val input = Path("inputs/2015/7.txt").readText()

    var start = System.nanoTime()
    var result = part1_7(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_7(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}