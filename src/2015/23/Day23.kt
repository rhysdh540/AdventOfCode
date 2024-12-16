import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_23(input: String): Any? {
    return mutableMapOf("a" to 0u, "b" to 0u).apply { Day23.run(this, input) }["b"]
}

fun part2_23(input: String): Any? {
    return mutableMapOf("a" to 1u, "b" to 0u).apply { Day23.run(this, input) }["b"]
}

object Day23 {
    fun run(regs: MutableMap<String, UInt>, input: String) {
        val insns = input.lines().map {
            val parts = it.split(' ')
            Triple(parts[0], parts[1].removeSuffix(","), parts.drop(2))
        }

        var i = 0
        while (i in insns.indices) {
            val (insn, reg, args) = insns[i]
            when (insn) {
                "hlf" -> regs[reg] = regs[reg]!! / 2u
                "tpl" -> regs[reg] = regs[reg]!! * 3u
                "inc" -> regs[reg] = regs[reg]!! + 1u
                "jmp" -> {
                    i += reg.toInt()
                    continue
                }
                "jie" -> if (regs[reg]!! % 2u == 0u) {
                    i += args[0].toInt()
                    continue
                }
                "jio" -> if (regs[reg]!! == 1u) {
                    i += args[0].toInt()
                    continue
                }
            }
            i++
        }
    }
}

fun main() {
    val input = Path("inputs/2015/23.txt").readText()

    var start = System.nanoTime()
    var result = part1_23(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_23(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}