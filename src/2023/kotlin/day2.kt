import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    var sum = 0
    lines.forEachIndexed { index, it ->
        val (r, g, b) = getRGB(it)
        if(r <= 12 && g <= 13 && b <= 14) {
            sum += index + 1
        }
    }

    return sum
}

private fun PuzzleInput.part2(): Any? {
    return lines.sumOf {
        val (r, g, b) = getRGB(it)
        r * g * b
    }
}

private fun getRGB(line: String): Triple<Int, Int, Int> {
    val line = line.substring(line.indexOf(':') + 2)
    var rgb = Triple(0, 0, 0)
    for(pull in line.split("; ")) {
        for(color in pull.split(", ")) {
            val num = color.substring(0, color.indexOf(' ')).toInt()
            when {
                color.endsWith("red") && num > rgb.first -> rgb = rgb.copy(first = num)
                color.endsWith("green") && num > rgb.second -> rgb = rgb.copy(second = num)
                color.endsWith("blue") && num > rgb.third -> rgb = rgb.copy(third = num)
            }
        }
    }

    return rgb
}

fun main() {
    val input = PuzzleInput(2023, 2)

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