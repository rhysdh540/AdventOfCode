import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_9(input: String): Any? {
    val blocks = mutableListOf<Int>()
    for ((index, char) in input.withIndex()) {
        val count = char.digitToInt()
        if (index % 2 == 0) {
            repeat(count) { blocks.add(index / 2) }
        } else {
            repeat(count) { blocks.add(-1) }
        }
    }

    while (blocks.contains(-1)) {
        val empty = blocks.indexOf(-1)
        val block = blocks.indexOfLast { it != -1 }
        blocks[empty] = blocks[block]
        blocks[block] = -1

        while (blocks.last() == -1) {
            blocks.removeAt(blocks.lastIndex)
        }
    }

    return blocks.withIndex().filter { it.value != -1 }.sumOf { it.index * it.value.toLong() }
}

fun part2_9(input: String): Any? {
    val blocks = mutableListOf<Int>()
    for ((index, char) in input.withIndex()) {
        val count = char.digitToInt()
        if (index % 2 == 0) {
            repeat(count) { blocks.add(index / 2) }
        } else {
            repeat(count) { blocks.add(-1) }
        }
    }

    // free spaces
    fun fs(blocks: List<Int>): List<Pair<Int, Int>> {
        val fs = mutableListOf<Pair<Int, Int>>()
        var start = -1
        for (i in blocks.indices) {
            if (blocks[i] == -1) {
                if (start == -1) start = i
            } else {
                if (start != -1) {
                    fs.add(Pair(start, i - 1))
                    start = -1
                }
            }
        }
        if (start != -1) {
            fs.add(Pair(start, blocks.lastIndex))
        }
        return fs
    }

    data class File(val id: Int, var start: Int, var end: Int, val len: Int)

    val files = (0..input.length / 2).map<Int, File> { id ->
        val start = blocks.indexOf(id)
        val end = blocks.lastIndexOf(id)
        File(id, start, end, (end - start + 1))
    }.sortedByDescending { it.id }.toMutableList()

    for (file in files) {
        val fs = fs(blocks)
        val span = fs.find { span ->
            span.first + file.len - 1 <= span.second && span.second < file.start
        }

        if (span != null) {
            val newStart = span.first
            val newEnd = newStart + file.len - 1

            for (i in 0 until file.len) {
                blocks[newStart + i] = file.id
            }

            for (i in file.start..file.end) {
                blocks[i] = -1
            }

            file.start = newStart
            file.end = newEnd
        }
    }

    return blocks.withIndex().filter { it.value != -1 }.sumOf { it.index * it.value.toLong() }
}

fun main() {
    val input = Path("inputs/2024/9.txt").readText()

    var start = System.nanoTime()
    var result = part1_9(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_9(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}