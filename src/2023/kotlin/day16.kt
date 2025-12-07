import dev.rdh.aoc.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.IntStream

private fun PuzzleInput.part1(): Any? {
    return shoot(grid, Beam(0, 0, 0, 1))
}

private fun PuzzleInput.part2(): Any? {
    val len = grid.size
    val wid = grid[0].size

    val futures = mutableListOf<CompletableFuture<Void>>()
    val max = AtomicInteger(-1)

    fun run(beam: Beam): () -> Unit = {
        val run = shoot(grid, beam)
        max.updateAndGet { current -> maxOf(current, run) }
    }

    IntStream.range(0, len).forEach { i ->
        futures.add(CompletableFuture.runAsync(run(Beam(i, 0, 0, 1))))
        futures.add(CompletableFuture.runAsync(run(Beam(i, wid - 1, 0, -1))))
    }

    IntStream.range(0, wid).forEach { i ->
        futures.add(CompletableFuture.runAsync(run(Beam(0, i, 1, 0))))
        futures.add(CompletableFuture.runAsync(run(Beam(len - 1, i, -1, 0))))
    }

    CompletableFuture.allOf(*futures.toTypedArray()).join()

    return max.get()
}

private data class Beam(var r: Int, var c: Int, var dr: Int, var dc: Int)

private fun shoot(grid: List2d<Char>, start: Beam): Int {
    val beams = ArrayDeque<Beam>()
    beams.add(start)
    val visitedStates = mutableSetOf<Triple<Int, Int, Vec2i>>()
    val rows = grid.size
    val cols = grid[0].size
    var count = 0

    val visited = Array(rows) { BooleanArray(cols) }

    while (beams.isNotEmpty()) {
        val beam = beams.removeFirst()

        // If out of bounds, skip
        if (beam.r < 0 || beam.r >= rows || beam.c < 0 || beam.c >= cols) continue

        // If we've visited this state already, skip
        val state = Triple(beam.r, beam.c, v(beam.dr, beam.dc))
        if (!visitedStates.add(state)) continue

        val c = grid[beam.r][beam.c]

        when (c) {
            '/' -> {
                val temp = beam.dr
                beam.dr = -beam.dc
                beam.dc = -temp
            }

            '\\' -> {
                val temp = beam.dr
                beam.dr = beam.dc
                beam.dc = temp
            }

            '|' -> {
                if (beam.dc != 0) {
                    beams.add(Beam(beam.r, beam.c, 1, 0))
                    beam.dr = -1
                    beam.dc = 0
                }
            }

            '-' -> {
                if (beam.dr != 0) {
                    beams.add(Beam(beam.r, beam.c, 0, 1))
                    beam.dr = 0
                    beam.dc = -1
                }
            }
        }

        if (!visited[beam.r][beam.c]) {
            visited[beam.r][beam.c] = true
            count++
        }

        beam.r += beam.dr
        beam.c += beam.dc

        beams.add(beam)
    }

    return count
}

fun main() = PuzzleInput(2023, 16).withSolutions({ part1() }, { part2() }).run()