import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_12(input: String): Any? {
    val numbers = Regex("-?\\d+").findAll(input).map { it.value.toInt() }.toList()
    return numbers.sum()
}

fun part2_12(input: String): Any? {
    return sum(Zson.parse<Map<String, Any>>(input)!!)
}

fun sum(json: Any): Int {
    return when (json) {
        is Int -> json
        is List<*> -> json.sumOf { sum(it!!) }
        is Map<*, *> -> {
            if (json.values.contains("red")) 0
            else json.values.sumOf { sum(it!!) }
        }
        else -> 0
    }
}

fun main() {
    val input = Path("inputs/2015/12.txt").readText()

    var start = System.nanoTime()
    var result = part1_12(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_12(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}