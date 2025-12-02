import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    var dial = 50
    var sum = 0
    for (line in lines) {
        val dir = line[0]
        val value = line.substring(1).toInt()
        dial += if (dir == 'R') value else -value
        if (dial % 100 == 0) sum++
    }

    return sum
}

private fun PuzzleInput.part2(): Any? {
    var dial = 50
    var sum = 0
    for (line in lines) {
        val right = line[0] == 'R'
        val value = line.substring(1).toInt()
        val (l, u) = if (right) {
            Pair(dial + 1, dial + value)
        } else {
            Pair(dial - value, dial - 1)
        }

        // ceil(l/100), floor(u/100) give the next 100 after l and the last 100 before u
        val (min, max) = Pair(Math.ceilDiv(l, 100), Math.floorDiv(u, 100))

        if (max >= min) sum += max - min + 1
        dial += if (right) value else -value
    }
    return sum
}

fun main() {
    val input = getInput(2025, 1)

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