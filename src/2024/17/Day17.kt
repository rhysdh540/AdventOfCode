import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_17(input: String): Any? {
    val parts = input.split("\n\n")
    val registers = parts[0].lines().associate {
        val (r, v) = Regex("""Register (.): (\d+)""").find(it)!!.destructured
        r to v.toInt()
    }.toMutableMap()

    val instructions = parts[1].drop("Program: ".length).split(",").map { it.toInt() }

    val output = Day17.runProgram(registers, instructions)
    return output.joinToString(",")
}

fun part2_17(input: String): Any? {
    val parts = input.split("\n\n")
    val instructions = parts[1].drop("Program: ".length).split(",").map { it.toInt() }

    val a = Day17.reconstructInitialA(instructions)
    val run = Day17.runProgram(mutableMapOf("A" to a.toInt(), "B" to 0, "C" to 0), instructions)
    if(run != instructions) {
        error("Reconstructed A value: $a is incorrect")
    }

    return a
}

object Day17 {
    fun runProgram(registers: MutableMap<String, Int>, instructions: List<Int>): List<Int> {
        fun getComboValue(operand: Int, registers: Map<String, Int>): Int {
            return when (operand) {
                in 0..3 -> operand
                4 -> registers["A"]!!
                5 -> registers["B"]!!
                6 -> registers["C"]!!
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
                    val power = getComboValue(operand, registers)
                    registers["A"] = registers["A"]!! / (1 shl power)
                }
                1 -> { // bxl
                    registers["B"] = registers["B"]!! xor operand
                }
                2 -> { // bst
                    registers["B"] = getComboValue(operand, registers) % 8
                }
                3 -> { // jnz
                    if (registers["A"] != 0) {
                        i = operand
                        continue
                    }
                }
                4 -> { // bxc
                    registers["B"] = registers["B"]!! xor registers["C"]!!
                }
                5 -> { // out
                    output.add(getComboValue(operand, registers) % 8)
                }
                6 -> { // bdv
                    val power = getComboValue(operand, registers)
                    registers["B"] = registers["A"]!! / (1 shl power)
                }
                7 -> { // cdv
                    val power = getComboValue(operand, registers)
                    registers["C"] = registers["A"]!! / (1 shl power)
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
            while (runProgram(mutableMapOf("A" to a, "B" to 0, "C" to 0), desiredOutput) != shortenedProgram) {
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