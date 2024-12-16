import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_2(input: String): Any? {
    val insns = input.lines().map { it.toCharArray() }
    val keypad = arrayOf(
        arrayOf('1', '2', '3'),
        arrayOf('4', '5', '6'),
        arrayOf('7', '8', '9')
    )

    var (x, y) = 1 to 1
    val code = mutableListOf<Char>()

    for (insn in insns) {
        for (c in insn) {
            when (c) {
                'U' -> if (y > 0) y--
                'D' -> if (y < 2) y++
                'L' -> if (x > 0) x--
                'R' -> if (x < 2) x++
            }
        }
        code.add(keypad[y][x])
    }

    return code.joinToString("")
}

fun part2_2(input: String): Any? {
    val insns = input.lines().map { it.toCharArray() }
    val keypad = arrayOf(
        arrayOf(' ', ' ', '1', ' ', ' '),
        arrayOf(' ', '2', '3', '4', ' '),
        arrayOf('5', '6', '7', '8', '9'),
        arrayOf(' ', 'A', 'B', 'C', ' '),
        arrayOf(' ', ' ', 'D', ' ', ' ')
    )

    var (x, y) = 0 to 2
    val code = mutableListOf<Char>()

    for (insn in insns) {
        for (c in insn) {
            val (nx, ny) = when (c) {
                'U' -> x to (y - 1)
                'D' -> x to (y + 1)
                'L' -> (x - 1) to y
                'R' -> (x + 1) to y
                else -> error("Invalid instruction")
            }

            if (nx in 0..4 && ny in 0..4 && keypad[ny][nx] != ' ') {
                x = nx
                y = ny
            }
        }
        code.add(keypad[y][x])
    }

    return code.joinToString("")
}

fun main() {
    val input = Path("inputs/2016/2.txt").readText()

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