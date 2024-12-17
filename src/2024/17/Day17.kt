import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_17(input: String): Any? {
    val parts = input.split("\n\n")
    val registers = parts[0].lines().associate {
        val (r, v) = Regex("""Register (.): (\d+)""").find(it)!!.destructured
        r to v.toLong()
    }.toMutableMap()

    val instructions = parts[1].drop("Program: ".length).split(",").map { it.toInt() }

    val t = Triple(registers["A"]!!, registers["B"]!!, registers["C"]!!)
    val output = Day17.interpret(t, instructions)
    return output.joinToString(",")
}

fun part2_17(input: String): Any? {
    val parts = input.split("\n\n")
    val instructions = parts[1].drop("Program: ".length).split(",").map { it.toInt() }

    var a1 = 0L
    for (i in instructions.indices.reversed()) {
        a1 = a1 shl 3 // move to the left by 3 bits to make room for the next value
        val shortenedProgram = instructions.drop(i)
        while (Day17.interpret(Triple(a1, 0, 0), instructions) != shortenedProgram) {
            a1++
        }
    }
    val a = a1
    val run = Day17.interpret(Triple(a, 0, 0), instructions)
    if (run != instructions) {
        error("Reconstructed A value: $a is incorrect")
    }

    return a
}

object Day17 {
    fun interpret(registers: Triple<Long, Long, Long>, instructions: List<Int>): List<Int> {
        var (a, b, c) = registers

        fun getComboValue(operand: Int): Long {
            return when (operand) {
                in 0..3 -> operand.toLong()
                4 -> a
                5 -> b
                6 -> c
                else -> error("Invalid combo operand: $operand")
            }
        }

        var i = 0
        val output = mutableListOf<Int>()
        while (i < instructions.size) {
            if (i + 1 >= instructions.size) break

            val opcode = instructions[i]
            val operand = instructions[i + 1]

            when (opcode) {
                0 -> { // adv
                    a = a / (1 shl getComboValue(operand).toInt())
                }
                1 -> { // bxl
                    b = b xor operand.toLong()
                }
                2 -> { // bst
                    b = getComboValue(operand) % 8
                }
                3 -> { // jnz
                    if (a != 0L) {
                        i = operand
                        continue
                    }
                }
                4 -> { // bxc
                    b = b xor c
                }
                5 -> { // out
                    output.add((getComboValue(operand) % 8).toInt())
                }
                6 -> { // bdv
                    b = a / (1 shl getComboValue(operand).toInt())
                }
                7 -> { // cdv
                    c = a / (1 shl getComboValue(operand).toInt())
                }
                else -> error("Unknown opcode: $opcode at position $i")
            }

            i += 2
        }

        return output
    }
}

fun main() {
    val input = Path("inputs/2024/17.txt").readText()

    var start = System.nanoTime()
    var result = part1_17(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_17(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}