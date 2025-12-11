import dev.rdh.aoc.*
import kotlin.collections.sumOf

private fun PuzzleInput.part1() = solve("you", true, true)
private fun PuzzleInput.part2() = solve("svr", false, false)

private fun PuzzleInput.solve(src: String, dac: Boolean, fft: Boolean): Long {
    val graph = lines.associate { line ->
        line.split(' ').let { it[0].dropLast(1) to it.drop(1) }
    }

    data class State(val d: String, val dac: Boolean, val fft: Boolean)

    val memo = mutableMapOf<State, Long>()
    fun dfs(node: String, dac: Boolean, fft: Boolean): Long {
        val state = State(node, dac, fft)
        memo[state]?.let { return it }

        val result = when (node) {
            "out" -> if (dac && fft) 1 else 0
            else -> {
                val neighbors = graph[node] ?: emptyList()
                neighbors.sumOf { neighbor ->
                    dfs(neighbor, dac || neighbor == "dac", fft || neighbor == "fft")
                }
            }
        }

        memo[state] = result
        return result
    }

    return dfs(src, dac, fft)
}

fun main() = PuzzleInput(2025, 11).withSolutions({ part1() }, { part2() }).run()