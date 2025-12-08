import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (coords, edges, find, union) = parse()

    repeat(1000) {
        val e = edges[it]
        union(e.a, e.b)
    }

    val counts = IntArray(coords.size)
    for (i in coords.indices) {
        counts[find(i)]++
    }

    return counts.sortedDescending().take(3).product()
}

private fun PuzzleInput.part2(): Any? {
    val (coords, edges, find, union) = parse()
    var numCircuits = coords.size
    lateinit var lastEdge: Edge

    for (e in edges) {
        if (find(e.a) != find(e.b)) {
            lastEdge = e
            union(e.a, e.b)
            numCircuits--

            if (numCircuits == 1) break
        }
    }

    val a = coords[lastEdge.a]
    val b = coords[lastEdge.b]
    return a.x.toLong() * b.x.toLong()
}

private data class Edge(val a: Int, val b: Int, val dist2: Long)

private data class Parsed(
    val coords: List<Vec3i>,
    val edges: List<Edge>,
    val find: (Int) -> Int,
    val union: (Int, Int) -> Unit
)

private fun PuzzleInput.parse(): Parsed {
    val coords = lines.map {
        it.split(',').ints.toVec3()
    }

    val parent = IntArray(coords.size) { it }
    val rank = IntArray(coords.size)

    fun find(x: Int): Int {
        if (parent[x] != x) {
            parent[x] = find(parent[x])
        }
        return parent[x]
    }

    fun union(a: Int, b: Int) {
        val pa = find(a)
        val pb = find(b)
        if (pa == pb) return

        if (rank[pa] < rank[pb]) {
            parent[pa] = pb
        } else if (rank[pb] < rank[pa]) {
            parent[pb] = pa
        } else {
            parent[pb] = pa
            rank[pa]++
        }
    }

    val edges = mutableListOf<Edge>()

    for (i in coords.indices) {
        val a = coords[i]
        for (j in i + 1 until coords.size) {
            val b = coords[j]
            edges += Edge(i, j, a.dist2(b))
        }
    }

    edges.sortBy { it.dist2 }
    return Parsed(coords, edges, ::find, ::union)
}

fun main() = PuzzleInput(2025, 8).withSolutions({ part1() }, { part2() }).run()