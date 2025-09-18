import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return input.split(',').sumOf { it.hash() }
}

private fun PuzzleInput.part2(): Any? {
    val boxes = Array(256) { linkedMapOf<String, Int>() }

    input.split(',').forEach { insn ->
        val remove = insn.contains('-')
        val index = if (remove) insn.indexOf('-') else insn.indexOf('=')
        val key = insn.take(index)

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

private fun String.hash(): Int {
    var current = 0
    for(c in this) {
        current += c.code
        current *= 17
        current %= 256
    }

    return current
}

fun main() {
    val input = getInput(2023, 15)

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