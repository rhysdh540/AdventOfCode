import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    var dial = 50
    var sum = 0
    for (line in lines) {
        val dir = line[0]
        val value = line.substring(1).toInt()
        when (dir) {
            'L' -> dial = (dial - value) % 100
            'R' -> dial = (dial + value) % 100
        }

        if (dial == 0) sum++
    }

    return sum
}

private fun PuzzleInput.part2(): Any? {
    var dial = 50
    var sum = 0
    for (line in lines) {
        val dir = line[0]
        val value = line.substring(1).toInt()
        val inc = if (dir == 'L') -1 else 1
        repeat(value) {
            dial += inc
            if (dial < 0) dial += 100
            if (dial >= 100) dial -= 100
            if (dial == 0) sum++
        }
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