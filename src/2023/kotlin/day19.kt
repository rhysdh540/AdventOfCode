import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (workflowsS, partsS) = sections.map { it.lines() }
    val workflows = workflowsS.associate {
        // in format name{rule,rule,...}
        val name = it.substringBefore("{")
        val rules = it.substringAfter("{").substringBefore("}").split(",").map { Rule(it) }
        name to rules
    }

    val parts = partsS.map {
        val (x, m, a, s) = it.removePrefix("{").removeSuffix("}").split(",")
        Part(
            x.removePrefix("x=").toInt(),
            m.removePrefix("m=").toInt(),
            a.removePrefix("a=").toInt(),
            s.removePrefix("s=").toInt()
        )
    }

    val accepted = mutableSetOf<Part>()

    for (part in parts) {
        var currentWorkflow = "in"

        while (true) {
            val rules = workflows[currentWorkflow] ?: error("No workflow named $currentWorkflow")
            val rule = rules.firstOrNull { it.predicate(part) } ?: error("No rule matched for $part in workflow $currentWorkflow")
            if (rule.output == "A") {
                accepted.add(part)
                break
            } else if (rule.output == "R") {
                break
            } else {
                currentWorkflow = rule.output
            }
        }
    }

    return accepted.sumOf { it.x + it.m + it.a + it.s }
}

private fun PuzzleInput.part2(): Any? {
    data class Box(val xr: IntRange, val mr: IntRange, val ar: IntRange, val sr: IntRange) {
        fun count(): Long =
            xr.count().toLong() * mr.count() * ar.count() * sr.count()
    }

    data class Rule2(val field: Char?, val op: Char?, val value: Int?, val out: String)

    fun parseRule(raw: String): Rule2 {
        val parts = raw.split(":")
        return if (parts.size == 1) {
            Rule2(null, null, null, raw)
        } else {
            val cond = parts[0]
            val out = parts[1]
            val op = if (cond.contains("<")) '<' else '>'
            val (f, v) = cond.split(op)
            Rule2(f[0], op, v.toInt(), out)
        }
    }

    val (workflowsS, _) = sections.map { it.lines() }
    val workflows: Map<String, List<Rule2>> = workflowsS.associate {
        val name = it.substringBefore("{")
        val rules = it.substringAfter("{").substringBefore("}").split(",").map(::parseRule)
        name to rules
    }

    fun split(box: Box, rule: Rule2): Pair<Box?, Box?> {
        val field = rule.field ?: return box to null
        val op = rule.op!!
        val v = rule.value!!

        fun IntRange.intersect(other: IntRange): IntRange? {
            val s = maxOf(first, other.first)
            val e = minOf(last, other.last)
            return if (s <= e) s..e else null
        }

        val get = when (field) {
            'x' -> box.xr
            'm' -> box.mr
            'a' -> box.ar
            's' -> box.sr
            else -> error("bad field")
        }
        val trueRange: IntRange
        val falseRange: IntRange
        if (op == '<') {
            trueRange = get.first..<v
            falseRange = v..get.last
        } else {
            trueRange = get.first..<v
            falseRange = get.first..v
        }

        val tR = get.intersect(trueRange)
        val fR = get.intersect(falseRange)
        val tBox = tR?.let {
            when (field) {
                'x' -> box.copy(xr = it)
                'm' -> box.copy(mr = it)
                'a' -> box.copy(ar = it)
                's' -> box.copy(sr = it)
                else -> box
            }
        }
        val fBox = fR?.let {
            when (field) {
                'x' -> box.copy(xr = it)
                'm' -> box.copy(mr = it)
                'a' -> box.copy(ar = it)
                's' -> box.copy(sr = it)
                else -> box
            }
        }
        return tBox to fBox
    }

    val initial = Box(1..4000, 1..4000, 1..4000, 1..4000)

    fun dfs(workflow: String, box: Box): Long {
        return when (workflow) {
            "A" -> box.count()
            "R" -> 0L
            else -> {
                var remaining: Box? = box
                var acc = 0L
                for (rule in workflows.getValue(workflow)) {
                    if (remaining == null) break
                    if (rule.field == null) {
                        acc += dfs(rule.out, remaining)
                        remaining = null
                    } else {
                        val (passBox, failBox) = split(remaining, rule)
                        if (passBox != null) acc += dfs(rule.out, passBox)
                        remaining = failBox
                    }
                }
                acc
            }
        }
    }

    return dfs("in", initial)
}

private data class Part(val x: Int, val m: Int, val a: Int, val s: Int)

private data class Rule(val predicate: (Part) -> Boolean, val output: String) {
    constructor(input: String) : this(
        predicate = Unit.run {
            val parts = input.split(":")
            if (parts.size == 1) {
                return@run { true }
            } else {
                val condition = parts[0]
                val operator = if (condition.contains("<")) "<" else ">"
                val (field, value) = condition.split(operator)
                val intValue = value.toInt()
                when (field) {
                    "x" -> if (operator == "<") {
                        return@run { part: Part -> part.x < intValue }
                    } else {
                        return@run { part: Part -> part.x > intValue }
                    }

                    "m" -> if (operator == "<") {
                        return@run { part: Part -> part.m < intValue }
                    } else {
                        return@run { part: Part -> part.m > intValue }
                    }

                    "a" -> if (operator == "<") {
                        return@run { part: Part -> part.a < intValue }
                    } else {
                        return@run { part: Part -> part.a > intValue }
                    }

                    "s" -> if (operator == "<") {
                        return@run { part: Part -> part.s < intValue }
                    } else {
                        return@run { part: Part -> part.s > intValue }
                    }

                    else -> throw IllegalArgumentException("Unknown field: $field")
                }
            }
        },
        output = Unit.run {
            val parts = input.split(":")
            if (parts.size == 1) {
                input
            } else {
                parts[1]
            }
        }
    )
}

fun main() {
    val input = PuzzleInput(2023, 19)

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