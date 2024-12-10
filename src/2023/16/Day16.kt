import Day16.Beam
import Day16.makeGrid
import Day16.shoot
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.IntStream
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_16(input: String): Any? {
    return shoot(makeGrid(input.lines()), Beam(0, 0, 0, 1))
}

fun part2_16(input: String): Any? {
    val grid = makeGrid(input.lines())

    val len = grid.size
    val wid = grid[0].size

    val futures = mutableListOf<CompletableFuture<Void>>()
    val max = AtomicInteger(-1)

    fun run(beam: Beam): () -> Unit = {
        val run = shoot(grid, beam)
        if (run > max.get()) {
            max.set(run)
        }
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

object Day16 {
    fun makeGrid(input: List<String>): Array<CharArray> {
        val len = input.size
        val wid = input[0].length
        val grid = Array<CharArray>(len) { CharArray(wid) }
        for (i in 0..<len) {
            grid[i] = input[i].toCharArray()
        }
        return grid
    }

    fun shoot(grid: Array<CharArray>, start: Beam): Int {
        val beams = ArrayDeque<Beam>()
        beams.add(start)
        val visitedStates = mutableSetOf<Triple<Int, Int, Pair<Int, Int>>>()
        val rows = grid.size
        val cols = grid[0].size
        var count = 0

        val visited = Array(rows) { BooleanArray(cols) }

        while (beams.isNotEmpty()) {
            val beam = beams.removeFirst()

            // If out of bounds, skip
            if (beam.r < 0 || beam.r >= rows || beam.c < 0 || beam.c >= cols) continue

            // If we've visited this state already, skip
            val state = Triple(beam.r, beam.c, Pair(beam.dr, beam.dc))
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

    data class Beam(var r: Int, var c: Int, var dr: Int, var dc: Int)
}

fun main() {
    val input = Path("inputs/2023/16.txt").readText()

    var start = System.nanoTime()
    var result = part1_16(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_16(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}