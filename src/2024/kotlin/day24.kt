import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (x, l) = splitBy(blankLines)

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

    val gates = splitBy(blankLines)[1].lines().map {
        val (input1, operator, input2, _, output) = it.split(" ")
        Gate(input1, operator, input2, output)
    }.toMutableList()

    val bitAdders = mutableListOf<BitAdder>()

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

    // put your answers here
    val swaps = setOf<Pair<String, String>>(

    )

    for((a, b) in swaps) {
        val firstGate = gates.single { it.output == a }
        val secondGate = gates.single { it.output == b }
        gates.remove(firstGate)
        gates.remove(secondGate)

        val newFirstGate = Gate(firstGate.input1, firstGate.operator, firstGate.input2, b)
        val newSecondGate = Gate(secondGate.input1, secondGate.operator, secondGate.input2, a)
        gates.add(newFirstGate)
        gates.add(newSecondGate)
    }

    for (i in 0..44) {
        //println("// Bit $i")
        val (x, y) = i.toString().padStart(2, '0').let { "x$it" to "y$it" }

        val pSum = gates.firstOrNull { it.lineMatches(x, "XOR", y) }?.output
            ?: error("pSum not found for bit $i, $x XOR $y")
        //println("$x XOR $y -> $pSum // pSum")

        val gCarry = gates.firstOrNull { it.lineMatches(x, "AND", y) }?.output
            ?: error("gCarry not found for bit $i, $x AND $y")
        //println("$x AND $y -> $gCarry // gCarry")

        val cin = if (i != 0) bitAdders[i - 1].zCarry else {
            // special case 0 since it has no cin, gCarry is the zCarry
            bitAdders.add(BitAdder(pSum, gCarry, "", gCarry, pSum)) // doesn't really matter what's what as long as the BitAdder's zCarry is gCarry
            //println()
            continue
        }

        val cCarry = gates.firstOrNull { it.lineMatches(pSum, "AND", cin) }?.output
            ?: error("cCarry not found for bit $i, $pSum AND $cin")
        //println("$pSum AND $gCarry -> $cin // cCarry")

        val zCarry = gates.firstOrNull { it.lineMatches(gCarry, "OR", cCarry) }?.output
            ?: error("zCarry not found for bit $i, $gCarry OR $cCarry")
        //println("$gCarry OR $cCarry -> $zCarry // zCarry")

        val zSum = gates.firstOrNull { it.lineMatches(pSum, "XOR", cin) }?.output
            ?: error("zSum not found for bit $i, $pSum XOR $cin")
        //println("$pSum XOR $cin -> $zSum // zSum")

        bitAdders.add(BitAdder(pSum, gCarry, cCarry, zCarry, zSum))
        //println()
    }

    return swaps.flatMap { it.toList() }.sorted().joinToString(",")
}

fun main() {
    val input = getInput(2024, 24)

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