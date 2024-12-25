import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_23(input: String): Any? {
    val connections = input.lines()
        .flatMap { it.split("-").let { (a, b) -> listOf(a to b, b to a) } }
        .groupBy({ it.first }, { it.second })

    return connections.entries.sumOf { (computer, neighbors) ->
        neighbors.sumOf { n ->
            neighbors.intersect(connections[n]!!).count { computer[0] == 't' || n[0] == 't' || it[0] == 't' }
        }
    }.div(6)
}

fun part2_23(input: String): String {
    val connections = input.lines()
        .flatMap { it.split("-").let { (a, b) -> listOf(a to b, b to a) } }
        .groupBy({ it.first }, { it.second })

    // Bron-Kerbosch algorithm
    fun bkMax(
        r: Set<String> = mutableSetOf(), // Current clique
        p: Set<String>,                  // Potential candidates
        x: Set<String> = mutableSetOf(), // Excluded candidates
    ): Set<String> {
        p as MutableSet; x as MutableSet

        if (p.isEmpty() && x.isEmpty()) {
            return r // r is a maximal clique
        }

        val pivot = (p union x).first()
        val neighborsOfPivot = connections[pivot]!!

        var max = r
        for (v in p - neighborsOfPivot) {
            val neighbors = connections[v]!!
            val new = bkMax(
                r + v,
                p intersect neighbors,
                x intersect neighbors,
            )

            if (new.size > max.size) {
                max = new
            }

            p.remove(v)
            x.add(v)
        }

        return max
    }

    return bkMax(p = connections.keys.toSet()).sorted().joinToString(",")
}

fun main() {
    val input = Path("inputs/2024/23.txt").readText()

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