import Day15.hash
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_15(input: String): Any? {
    return input.split(',').sumOf { it.hash() }
}

fun part2_15(input: String): Any? {
    val boxes = Array(256) { linkedMapOf<String, Int>() }

    input.split(',').forEach { insn ->
        val remove = insn.contains('-')
        val index = if (remove) insn.indexOf('-') else insn.indexOf('=')
        val key = insn.substring(0, index)

        val box = boxes[key.hash()]
        if (remove) {
            box.remove(key)
        } else {
            box[key] = insn.last().digitToInt()
        }
    }

    return boxes.withIndex().sumOf { (i, box) ->
        box.values.withIndex().sumOf { (j, v) ->
            (i + 1) * (j + 1) * v
        }
    }
}

object Day15 {
    fun String.hash(): Int {
        var current = 0
        for(c in this) {
            current += c.code
            current *= 17
            current %= 256
        }

        return current
    }
}

fun main() {
    val input = Path("inputs/2023/15.txt").readText()

    var start = System.nanoTime()
    var result = part1_15(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_15(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}