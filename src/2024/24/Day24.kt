import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_24(input: String): Any? {
    val (x, l) = input.split("\n\n")

    val mappings = mutableMapOf<String, () -> Boolean>()
    for (line in x.lines()) {
        val (a, b) = line.split(": ")
        mappings.put(a) { b == "1" }
    }

    for (line in l.lines()) {
        val (a, op, b, _, result) = line.split(" ")
        mappings.put(result) {
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

fun part2_24(input: String): Any? {
    fun String.lineMatches(operand1: String, operator: String, operand2: String): Boolean {
        return this.startsWith("$operand1 $operator $operand2 ->") || this.startsWith("$operand2 $operator $operand1 ->")
    }

    fun String.lineOutput() = this.split(" -> ")[1]

    data class BitAdder(
        val pSum: String,
        val gCarry: String,
        val cCarry: String,
        val zCarry: String,
        val zSum: String
    )

    val bitAdders = mutableListOf<BitAdder>()

    // this is a very manual solution
    // first understand how a bit adder works
    // then run this program and figure out which wires need to be swapped when it errors
    // add the swaps to the set at the bottom
    // repeat until it works (should be 4 times i hope)

    // each bit looks like this: (where 00 is the bit number, and cin is the zCarry of the previous bit)
    // x00 XOR y00 -> pSum
    // x00 AND y00 -> gCarry
    // pSum AND cin -> cCarry
    // gCarry OR cCarry -> zCarry
    // pSum XOR cin -> z00

    // put your answers here
    val swaps = setOf<Pair<String, String>>(

    )

    for (i in 0..44) {
        println("// Bit $i")
        val iPadded = i.toString().padStart(2, '0')

        val pSum = input.lines().firstOrNull { it.lineMatches("x$iPadded", "XOR", "y$iPadded") }?.lineOutput()
            ?: error("pSum not found for bit $i, x$iPadded XOR y$iPadded")
        println("x$iPadded XOR y$iPadded -> $pSum // pSum")

        val gCarry = input.lines().firstOrNull { it.lineMatches("x$iPadded", "AND", "y$iPadded") }?.lineOutput()
            ?: error("gCarry not found for bit $i, x$iPadded AND y$iPadded")
        println("x$iPadded AND y$iPadded -> $gCarry // gCarry")

        val cin = if (i != 0) bitAdders[i - 1].zCarry else {
            // special case 0 since it has no cin, gCarry is the zCarry
            bitAdders.add(BitAdder(pSum, gCarry, "", gCarry, pSum)) // doesn't really matter what's what as long as the BitAdder's zCarry is gCarry
            println()
            continue
        }

        val cCarry = input.lines().firstOrNull { it.lineMatches(pSum, "AND", cin) }?.lineOutput()
            ?: error("cCarry not found for bit $i, $pSum AND $cin")
        println("$pSum AND $gCarry -> $cin // cCarry")

        val zCarry = input.lines().firstOrNull { it.lineMatches(gCarry, "OR", cCarry) }?.lineOutput()
            ?: error("zCarry not found for bit $i, $gCarry OR $cCarry")
        println("$gCarry OR $cCarry -> $zCarry // zCarry")

        val zSum = input.lines().firstOrNull { it.lineMatches(pSum, "XOR", cin) }?.lineOutput()
            ?: error("zSum not found for bit $i, $pSum XOR $cin")
        println("$pSum XOR $cin -> $zSum // zSum")

        bitAdders.add(BitAdder(pSum, gCarry, cCarry, zCarry, zSum))
        println()
    }

    return swaps.flatMap { it.toList() }.sorted().joinToString(",")
}

fun main() {
    val input = Path("inputs/2024/24.txt").readText()

    var start = System.nanoTime()
    var result = part1_24(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_24(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}