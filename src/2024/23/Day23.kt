import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_23(input: String): Any? {
    val connections = input.lines()
        .flatMap { it.split("-").let { (a, b) -> listOf(a to b, b to a) } }
        .groupBy({ it.first }, { it.second })

    return connections.flatMap { (computer, neighbors) ->
        neighbors.map { n ->
            neighbors.intersect(connections[n]!!).count { it[0] == 't' || computer[0] == 't' || n[0] == 't' }
        }
    }.sum() / 6
}

fun part2_23(input: String): String {
    val connections = input.lines()
        .flatMap { it.split("-").let { (a, b) -> listOf(a to b, b to a) } }
        .groupBy({ it.first }, { it.second })

    val cliques = mutableListOf<Set<String>>()

    // Bron-Kerbosch algorithm
    fun bk(
        r: Set<String> = mutableSetOf(), // Current clique
        p: Set<String>,                  // Potential candidates
        x: Set<String> = mutableSetOf(), // Excluded candidates
    ) {
        if (p.isEmpty() && x.isEmpty()) {
            cliques.add(r)
            return
        }

        p as MutableSet; x as MutableSet

        val pivot = (p union x).first()
        val neighborsOfPivot = connections[pivot]!!

        for (v in p - neighborsOfPivot) {
            val neighbors = connections[v]!!
            bk(
                r + v,
                p intersect neighbors,
                x intersect neighbors
            )
            p.remove(v)
            x.add(v)
        }
    }

    bk(p = connections.keys.toSet())

    return cliques.maxBy { it.size }.sorted().joinToString(",")
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