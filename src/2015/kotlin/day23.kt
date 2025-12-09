import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return mutableMapOf("a" to 0u, "b" to 0u).also { solve(it) }["b"]
}

private fun PuzzleInput.part2(): Any? {
    return mutableMapOf("a" to 1u, "b" to 0u).also { solve(it) }["b"]
}

private fun PuzzleInput.solve(regs: MutableMap<String, UInt>) {
    val insns = lines.map {
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

fun main() = PuzzleInput(2015, 23).withSolutions({ part1() }, { part2() }).run()