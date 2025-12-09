import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (coords, edges, uf) = parse()

    repeat(1000) {
        val e = edges[it]
        uf.union(e.a, e.b)
    }

    val counts = IntArray(coords.size)
    for (i in coords.indices) {
        counts[uf.find(i)]++
    }

    return counts.sortedDescending().take(3).product()
}

private fun PuzzleInput.part2(): Any? {
    val (coords, edges, uf) = parse()
    var numCircuits = coords.size
    lateinit var lastEdge: Connection

    for (e in edges) {
        if (uf.find(e.a) != uf.find(e.b)) {
            lastEdge = e
            uf.union(e.a, e.b)
            numCircuits--

            if (numCircuits == 1) break
        }
    }

    val a = coords[lastEdge.a]
    val b = coords[lastEdge.b]
    return a.x.toLong() * b.x.toLong()
}

private data class Connection(val a: Int, val b: Int, val dist2: Long)

private fun PuzzleInput.parse(): Triple<List<Vec3i>, List<Connection>, UnionFind> {
    val coords = lines.map {
        it.split(',').ints.toVec3()
    }

    val edges = mutableListOf<Connection>()

    for (i in coords.indices) {
        val a = coords[i]
        for (j in i + 1 until coords.size) {
            val b = coords[j]
            edges += Connection(i, j, a.dist2(b))
        }
    }

    edges.sortBy { it.dist2 }
    return Triple(coords, edges, UnionFind(coords.size))
}

fun main() = PuzzleInput(2025, 8).withSolutions({ part1() }, { part2() }).run()