import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_2(input: String): Any? {
    var sum = 0
    input.lines().forEachIndexed { index, it ->
        val (r, g, b) = Day2.getRGB(it)
        if(r <= 12 && g <= 13 && b <= 14) {
            sum += index + 1
        }
    }

    return sum
}

fun part2_2(input: String): Any? {
    var sum = 0
    input.lines().forEach {
        val (r, g, b) = Day2.getRGB(it)
        sum += r * g * b
    }

    return sum
}

object Day2 {
    fun getRGB(line: String): Triple<Int, Int, Int> {
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
}

fun main() {
    val input = Path("inputs/2023/2.txt").readText()

    var start = System.nanoTime()
    var result = part1_2(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_2(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}