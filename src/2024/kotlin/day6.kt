@file:OptIn(ExperimentalAtomicApi::class)

import dev.rdh.aoc.*
import java.util.BitSet
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement

private fun PuzzleInput.part1(): Any? {
    var startPos = v(0, 0)
    val width = grid[0].size
    val height = grid.size

    val grid = BooleanArray(width * height) { index ->
        val x = index / width
        val y = index % width
        val c = grid[x][y]
        if (c == '^') {
            startPos = v(x, y)
        }
        c == '#'
    }

    val visitedPositions = getVisitedPositions(grid, width, height, startPos)
    return visitedPositions.cardinality()
}

private fun PuzzleInput.part2(): Any? {
    var startPos = v(0, 0)
    val width = grid[0].size
    val height = grid.size

    val grid = BooleanArray(width * height) { index ->
        val x = index / width
        val y = index % width
        val c = grid[x][y]
        if (c == '^') {
            startPos = v(x, y)
        }
        c == '#'
    }

    val baseVisited = getVisitedPositions(grid, width, height, startPos)
        .toIntArray()
    val nthreads = Runtime.getRuntime().availableProcessors()
    val npositions = baseVisited.size / nthreads
    val output = AtomicInt(0)
    val threads = Array(nthreads) { t ->
        CompletableFuture.supplyAsync {
            val start = t * npositions
            val end = if (t == nthreads - 1) baseVisited.size else (t + 1) * npositions
            p2worker(
                grid,
                startPos,
                baseVisited,
                start until end,
                width,
                height,
                output
            )
        }
    }

    CompletableFuture.allOf(*threads).join()
    return output.load()
}

private fun p2worker(
    grid: BooleanArray,
    start: Pair<Int, Int>,
    allVisited: IntArray,
    range: IntRange,
    width: Int,
    height: Int,
    output: AtomicInt
) {
    for (i in range) {
        val sx = allVisited[i] / width
        val sy = allVisited[i] % width

        var (x, y) = start
        var direction = 0
        val visitedStates = BitSet() // (x, y) << 2 | direction
        while (true) {
            val key = ((x * width + y) shl 2) or direction
            if (visitedStates[key]) {
                output.fetchAndIncrement()
                break
            }

            visitedStates.set(key)

            var nx = x
            var ny = y
            when (direction) {
                0 -> nx--
                1 -> ny++
                2 -> nx++
                3 -> ny--
            }

            if (nx !in 0..<height || ny !in 0..<width) {
                break
            }

            if (grid[nx * width + ny] || (nx == sx && ny == sy)) {
                direction = (direction + 1) and 3
            } else {
                x = nx
                y = ny
            }
        }
    }
}

private fun getVisitedPositions(grid: BooleanArray, width: Int, height: Int, startPos: Pair<Int, Int>): BitSet {
    var (x, y) = startPos
    var direction = 0
    val visitedPositions = BitSet()
    while (true) {
        visitedPositions.set(x * width + y)

        var nx = x
        var ny = y
        when (direction) {
            0 -> nx--
            1 -> ny++
            2 -> nx++
            3 -> ny--
        }

        if (nx !in 0..<height || ny !in 0..<width) {
            return visitedPositions
        }

        if (grid[nx * width + ny]) {
            direction = (direction + 1) % 4
        } else {
            x = nx
            y = ny
        }
    }
}

fun main() = PuzzleInput(2024, 6).withSolutions({ part1() }, { part2() }).run()