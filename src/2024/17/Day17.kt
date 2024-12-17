import java.lang.invoke.MethodHandle
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_17(input: String): Any? {
    val parts = input.split("\n\n")
    val registers = parts[0].lines().associate {
        val (r, v) = Regex("""Register (.): (\d+)""").find(it)!!.destructured
        r to v.toInt()
    }.toMutableMap()

    val instructions = parts[1].drop("Program: ".length).split(",").map { it.toInt() }

    val t = Triple(registers["A"]!!, registers["B"]!!, registers["C"]!!)
    val output = Day17.runProgram(t, instructions)
    return output.joinToString(",")
}

fun part2_17(input: String): Any? {
    val parts = input.split("\n\n")
    val instructions = parts[1].drop("Program: ".length).split(",").map { it.toInt() }

    val a = Day17.reconstructInitialA(instructions)
//    val run = Day17.runProgram(mutableMapOf("A" to a.toInt(), "B" to 0, "C" to 0), instructions)
    val run = Day17.runProgram(Triple(a, 0, 0), instructions)
    if(run != instructions) {
        error("Reconstructed A value: $a is incorrect")
    }

    return a
}

object Day17 {
    fun runProgram(registers: Triple<Int, Int, Int>, instructions: List<Int>): List<Int> {
        var (a, b, c) = registers

        fun getComboValue(operand: Int): Int {
            return when (operand) {
                in 0..3 -> operand
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
                    val power = getComboValue(operand)
                    a = a / (1 shl power)
                }
                1 -> { // bxl
                    b = b xor operand
                }
                2 -> { // bst
                    b = getComboValue(operand) % 8
                }
                3 -> { // jnz
                    if (a != 0) {
                        i = operand
                        continue
                    }
                }
                4 -> { // bxc
                    b = b xor c
                }
                5 -> { // out
                    output.add(getComboValue(operand) % 8)
                }
                6 -> { // bdv
                    val power = getComboValue(operand)
                    b = a / (1 shl power)
                }
                7 -> { // cdv
                    val power = getComboValue(operand)
                    c = a / (1 shl power)
                }
                else -> error("Unknown opcode: $opcode at position $i")
            }
            i += 2
        }

        return output
    }

    fun reconstructInitialA(desiredOutput: List<Int>): Int {
        var a = 0

        for(i in desiredOutput.indices.reversed()) {
            a = a shl 3 // move to the left by 3 bits to make room for the next value
            val shortenedProgram = desiredOutput.drop(i)
            while (runProgram(Triple(a, 0, 0), desiredOutput) != shortenedProgram) {
                a++
            }

            println("Bit $i: ${a and 7}")
        }

        return a
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