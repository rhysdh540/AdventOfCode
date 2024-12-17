import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_8(input: String): Any? {
    return Day8.getScreen(input).sumOf { it.count { it } }
}

fun part2_8(input: String): Any? {
    return Day8.getScreen(input).joinToString("\n") { row ->
        row.joinToString("") { if (it) "\u2588\u2588" else "  " }
    }
}

object Day8 {
    fun getScreen(input: String): Array<BooleanArray> {
        val screen = Array(6) { BooleanArray(50) }
        input.lines().forEach {
            val parts = it.split(" ")
            if (parts[0] == "rect") {
                val (w, h) = parts[1].split("x").map { it.toInt() }
                for (i in 0 until h) {
                    for (j in 0 until w) {
                        screen[i][j] = true
                    }
                }
            } else if (parts[0] == "rotate") {
                val (dir, x, _, by) = parts.subList(1, parts.size)
                val i = x.split("=")[1].toInt()
                val b = by.toInt()
                when (dir) {
                    "row" -> {
                        val row = screen[i].toList()
                        for (j in 0 until screen[0].size) {
                            screen[i][(j + b) % screen[0].size] = row[j]
                        }
                    }

                    "column" -> {
                        val col = screen.map { it[i] }
                        for (j in 0 until screen.size) {
                            screen[(j + b) % screen.size][i] = col[j]
                        }
                    }

                    else -> throw IllegalArgumentException("Invalid direction: $dir")
                }
            } else throw IllegalArgumentException("Invalid instruction: $it")
        }

        return screen
    }
}

fun main() {
    val input = Path("inputs/2016/8.txt").readText()

    var start = System.nanoTime()
    var result = part1_8(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_8(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}