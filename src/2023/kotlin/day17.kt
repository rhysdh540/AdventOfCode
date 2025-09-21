import dev.rdh.aoc.PuzzleInput
import dev.rdh.aoc.getInput
import dev.rdh.aoc.plus
import java.util.*

private fun PuzzleInput.part1(): Any? {
    return run(intGrid) { states ->
        val forward = dirs[dir]
        val left = dirs[(dir + 3) % 4]
        val right = dirs[(dir + 1) % 4]

        // always can go left or right
        val leftCost = intGrid.getOrNull(pos.second + left.second)?.getOrNull(pos.first + left.first)
        if (leftCost != null) {
            states += State(pos + left, (dir + 3) % 4, 1, leftCost)
        }

        val rightCost = intGrid.getOrNull(pos.second + right.second)?.getOrNull(pos.first + right.first)
        if (rightCost != null) {
            states += State(pos + right, (dir + 1) % 4, 1, rightCost)
        }

        // can go forward if we haven't moved 3 times in this direction
        if (movesInDir < 3) {
            val forwardCost = intGrid.getOrNull(pos.second + forward.second)?.getOrNull(pos.first + forward.first)
            if (forwardCost != null) {
                states += State(pos + forward, dir, movesInDir + 1, forwardCost)
            }
        }
    }
}

private fun PuzzleInput.part2(): Any? {
    return run(intGrid) { states ->
        val forward = dirs[dir]
        val left = dirs[(dir + 3) % 4]
        val right = dirs[(dir + 1) % 4]

        // go left or right if >4 moves in this direction
        if (movesInDir >= 4) {
            val leftCost = intGrid.getOrNull(pos.second + left.second)?.getOrNull(pos.first + left.first)
            if (leftCost != null) {
                states += State(pos + left, (dir + 3) % 4, 1, leftCost)
            }

            val rightCost = intGrid.getOrNull(pos.second + right.second)?.getOrNull(pos.first + right.first)
            if (rightCost != null) {
                states += State(pos + right, (dir + 1) % 4, 1, rightCost)
            }
        }

        if (movesInDir < 10) {
            val forwardCost = intGrid.getOrNull(pos.second + forward.second)?.getOrNull(pos.first + forward.first)
            if (forwardCost != null) {
                states += State(pos + forward, dir, movesInDir + 1, forwardCost)
            }
        }
    }
}

private val dirs = listOf(
    Pair(1, 0), // right
    Pair(0, 1), // down
    Pair(-1, 0), // left
    Pair(0, -1) // up
)

data class State(val pos: Pair<Int, Int>, val dir: Int, val movesInDir: Int, val cost: Int)

private fun run(grid: List<List<Int>>, nextStates: State.(states: MutableList<State>) -> Unit): Int? {
    val target = Pair(grid[0].size - 1, grid.size - 1)
    val start = State(Pair(0, 0), 0, 0, 0)

    data class Key(val pos: Pair<Int, Int>, val dir: Int, val moves: Int)

    val queue = PriorityQueue<State>(compareBy { it.cost })
    val bestCost = mutableMapOf<Key, Int>()

    queue += start
    bestCost[Key(start.pos, start.dir, start.movesInDir)] = 0

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val curKey = Key(current.pos, current.dir, current.movesInDir)

        // Skip if this state is outdated
        if (current.cost != bestCost[curKey]) continue

        if (current.pos == target) {
            return current.cost
        }

        val nextStates = mutableListOf<State>()
        current.nextStates(nextStates)

        for (next in nextStates) {
            val nextKey = Key(next.pos, next.dir, next.movesInDir)
            val newCost = current.cost + next.cost
            val prev = bestCost[nextKey]
            if (prev == null || newCost < prev) {
                bestCost[nextKey] = newCost
                queue += State(next.pos, next.dir, next.movesInDir, newCost)
            }
        }
    }

    return null
}

fun main() {
    val input = getInput(2023, 17)

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