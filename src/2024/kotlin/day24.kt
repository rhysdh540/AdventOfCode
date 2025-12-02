import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (x, l) = sections

    val mappings = mutableMapOf<String, () -> Boolean>()
    for (line in x.lines()) {
        val (a, b) = line.split(": ")
        mappings[a] = { b == "1" }
    }

    for (line in l.lines()) {
        val (a, op, b, _, result) = line.split(" ")
        mappings[result] = {
            when (op) {
                "AND" -> mappings[a]!!.invoke() && mappings[b]!!.invoke()
                "OR" -> mappings[a]!!.invoke() || mappings[b]!!.invoke()
                "XOR" -> mappings[a]!!.invoke() xor mappings[b]!!.invoke()
                else -> error("unknown operator $op")
            }
        }
    }

    return mappings.keys.filter { it.startsWith("z") }.sortedDescending()
        .joinToString("") { if (mappings[it]!!.invoke()) "1" else "0" }.toLong(2)
}

private fun PuzzleInput.part2(): Any? {
    data class BitAdder(
        val pSum: String,
        val gCarry: String,
        val cCarry: String,
        val zCarry: String,
        val zSum: String
    )

    data class Gate(
        val input1: String,
        val operator: String,
        val input2: String,
        val output: String
    ) {
        fun lineMatches(input1: String, operator: String, input2: String): Boolean {
            if(this.operator != operator) return false
            return (this.input1 == input1 && this.input2 == input2) || (this.input1 == input2 && this.input2 == input1)
        }
    }

    val gates = sections[1].lines().map {
        val (input1, operator, input2, _, output) = it.split(" ")
        Gate(input1, operator, input2, output)
    }.toMutableSet()

    // this is a very manual solution
    // first understand how a bit adder works
    // then run this program and figure out which wires need to be swapped when it errors
    // add the swaps to the set below(the program will now automatically swap them)
    // repeat until it works (should be 4 times i hope)

    // each bit looks like this: (where 00 is the bit number, and cin is the zCarry of the previous bit)
    // x00 XOR y00 -> pSum
    // x00 AND y00 -> gCarry
    // pSum AND cin -> cCarry
    // gCarry OR cCarry -> zCarry
    // pSum XOR cin -> z00

    // insight from several months later:
    // for each swap, take a look at what it expects, and search fo an operation with one of the inputs and the right operator
    // then swap what it expects with what's actually there
    // so for example it expected `btr AND z07` but it was actually `btr AND bjm`, so swap `bjm` and `z07`
    // repeat
    // TODO: use this insight to automatically find swaps

    val swaps = mutableSetOf<Pair<String, String>>()

    while (true) {
        try {
            val bitAdders = mutableListOf<BitAdder>()

            for (i in 0..44) {
                val (x, y) = i.toString().padStart(2, '0').let { "x$it" to "y$it" }

                val pSum = gates.firstOrNull { it.lineMatches(x, "XOR", y) }?.output
                    ?: error("$x XOR $y")

                val gCarry = gates.firstOrNull { it.lineMatches(x, "AND", y) }?.output
                    ?: error("$x AND $y")

                val cin = if (i != 0) bitAdders[i - 1].zCarry else {
                    // special case 0 since it has no cin, gCarry is the zCarry
                    bitAdders.add(BitAdder(pSum, gCarry, "", gCarry, pSum)) // doesn't really matter what's what as long as the BitAdder's zCarry is gCarry
                    continue
                }

                val cCarry = gates.firstOrNull { it.lineMatches(pSum, "AND", cin) }?.output
                    ?: error("$pSum AND $cin")

                val zCarry = gates.firstOrNull { it.lineMatches(gCarry, "OR", cCarry) }?.output
                    ?: error("$gCarry OR $cCarry")

                val zSum = gates.firstOrNull { it.lineMatches(pSum, "XOR", cin) }?.output
                    ?: error("$pSum XOR $cin")

                bitAdders += BitAdder(pSum, gCarry, cCarry, zCarry, zSum)
            }
        } catch (e: Exception) {
            val (a, op, b) = e.message!!.spaced
            val expectedGate = gates.firstOrNull {
                // find a gate with the right operator and one of the inputs, but not the other
                it.operator == op
                        && (it.input1 == a || it.input2 == a || it.input1 == b || it.input2 == b)
                        && !(it.input1 == a && it.input2 == b) && !(it.input1 == b && it.input2 == a)
            } ?: error("no gate matches $a $op $b")

            // find out which input is wrong (expected and actual)
            val (expected, actual) = when {
                expectedGate.input1 == a || expectedGate.input1 == b -> expectedGate.input2 to (if (expectedGate.input1 == a) b else a)
                expectedGate.input2 == a || expectedGate.input2 == b -> expectedGate.input1 to (if (expectedGate.input2 == a) b else a)
                else -> error("neither input matches for $a $op $b")
            }

            // actually perform the swap
            swaps += expected to actual

            val firstGate = gates.single { it.output == expected }
            val secondGate = gates.single { it.output == actual }
            gates.remove(firstGate)
            gates.remove(secondGate)

            val newFirstGate = Gate(firstGate.input1, firstGate.operator, firstGate.input2, actual)
            val newSecondGate = Gate(secondGate.input1, secondGate.operator, secondGate.input2, expected)
            gates += newFirstGate
            gates += newSecondGate
            continue
        }

        break
    }

    return swaps.flatMap { it.toList() }.sorted().joinToString(",")
}

fun main() {
    val input = PuzzleInput(2024, 24)

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