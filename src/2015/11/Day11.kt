import Day11.increment
import Day11.isValid
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_11(input: String): Any? {
    var password = input.toCharArray()
    while (true) {
        password.increment()
        if (isValid(password)) {
            return String(password)
        }
    }
}

fun part2_11(input: String): Any? {
    return part1_11(part1_11(input) as String)
}

object Day11 {
    fun CharArray.increment() {
        for (i in size - 1 downTo 0) {
            if (this[i] == 'z') {
                this[i] = 'a'
            } else {
                this[i] = this[i] + 1
                break
            }
        }
    }

    fun isValid(password: CharArray): Boolean {
        val hasStraight = (0 until password.size - 2).any {
            password[it] + 1 == password[it + 1] && password[it + 1] + 1 == password[it + 2]
        }

        if (!hasStraight) return false

        val hasNoIOL = password.none { it == 'i' || it == 'o' || it == 'l' }

        if (!hasNoIOL) return false

        val hasTwoPairs = String(password).windowed(2).filter { it[0] == it[1] }.distinct().size >= 2

        return hasTwoPairs
    }
}

fun main() {
    val input = Path("inputs/2015/11.txt").readText()

    var start = System.nanoTime()
    var result = part1_11(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_11(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}