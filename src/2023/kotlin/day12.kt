import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val instructions = lines.map {
        val (record, allSizes) = it.split(' ')
        record to allSizes.split(',').map { it.toInt() }
    }

    return instructions.sumOf { arrange(it) }
}

private fun PuzzleInput.part2(): Any? {
    val instructions = input.lines().map {
        val (record, allSizes) = it.split(' ')
        record to allSizes.split(',').map { it.toInt() }
    }

    return instructions.sumOf { arrange(repeat(it, 5)) }
}

private data class Input(
    val record: Pair<String, List<Int>>,
    val index: Int,
    val numsIndex: Int,
    val numsRemaining: Int
) {
    override fun hashCode(): Int {
        return record.first.hashCode() * 31 + index * 31 + numsIndex * 31 + numsRemaining
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Input

        if (index != other.index) return false
        if (numsIndex != other.numsIndex) return false
        if (numsRemaining != other.numsRemaining) return false
        if (record != other.record) return false

        return true
    }
}

private fun arrange(
    record: Pair<String, List<Int>>,
    index: Int = 0,
    numsIndex: Int = -1,
    numsRemaining: Int = -1,
    cache: MutableMap<Input, Long> = mutableMapOf()
): Long {
    val input = Input(record, index, numsIndex, numsRemaining)
    if (input in cache) {
        return cache[input]!!
    }

    if (index == record.first.length) {
        return (if (numsRemaining <= 0 && numsIndex == record.second.size - 1) 1 else 0).toLong()
    }

    val c = record.first[index]
    var sum = 0L
    if (c == '?') {
        sum += arrangeWithChar(record, '#', index, numsIndex, numsRemaining, cache)
        sum += arrangeWithChar(record, '.', index, numsIndex, numsRemaining, cache)
    } else {
        sum += arrangeWithChar(record, c, index, numsIndex, numsRemaining, cache)
    }

    cache[input] = sum
    return sum
}

private fun arrangeWithChar(
    record: Pair<String, List<Int>>,
    c: Char,
    index: Int,
    numsIndex: Int,
    numsRemaining: Int,
    cache: MutableMap<Input, Long>
): Long {
    if (c == '#') {
        return if (numsRemaining == 0) { // no more # left
            0
        } else if (numsRemaining < 0) {
            if (numsIndex == record.second.size - 1) {
                0
            } else {
                arrange(record, index + 1, numsIndex + 1, record.second[numsIndex + 1] - 1, cache)
            }
        } else {
            arrange(record, index + 1, numsIndex, numsRemaining - 1, cache)
        }
    } else { // c == '.'
        return if (numsRemaining > 0) {
            0
        } else {
            arrange(record, index + 1, numsIndex, numsRemaining - 1, cache)
        }
    }
}

fun repeat(record: Pair<String, List<Int>>, times: Int): Pair<String, List<Int>> {
    return Pair(
        "${record.first}?".repeat(times).dropLast(1),
        (0 until times).flatMap { record.second }
    )
}

fun main() {
    val input = getInput(2023, 12)

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