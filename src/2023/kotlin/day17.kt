import dev.rdh.aoc.*
import java.util.*

private fun PuzzleInput.part1(): Any? {
    return solve(intGrid) { states ->
        val forward = dir.vec
        val left = dir.turnLeft().vec
        val right = dir.turnRight().vec

        // always can go left or right
        val leftCost = intGrid.getOrNull(pos.y + left.y)?.getOrNull(pos.x + left.x)
        if (leftCost != null) {
            states += State(pos + left, dir.turnLeft(), 1, leftCost)
        }

        val rightCost = intGrid.getOrNull(pos.y + right.y)?.getOrNull(pos.x + right.x)
        if (rightCost != null) {
            states += State(pos + right, dir.turnRight(), 1, rightCost)
        }

        // can go forward if we haven't moved 3 times in this direction
        if (movesInDir < 3) {
            val forwardCost = intGrid.getOrNull(pos.y + forward.y)?.getOrNull(pos.x + forward.x)
            if (forwardCost != null) {
                states += State(pos + forward, dir, movesInDir + 1, forwardCost)
            }
        }
    }
}

private fun PuzzleInput.part2(): Any? {
    return solve(intGrid) { states ->
        val forward = dir.vec
        val left = dir.turnLeft().vec
        val right = dir.turnRight().vec

        // go left or right if >4 moves in this direction
        if (movesInDir >= 4) {
            val leftCost = intGrid.getOrNull(pos.y + left.y)?.getOrNull(pos.x + left.x)
            if (leftCost != null) {
                states += State(pos + left, dir.turnLeft(), 1, leftCost)
            }

            val rightCost = intGrid.getOrNull(pos.y + right.y)?.getOrNull(pos.x + right.x)
            if (rightCost != null) {
                states += State(pos + right, dir.turnRight(), 1, rightCost)
            }
        }

        if (movesInDir < 10) {
            val forwardCost = intGrid.getOrNull(pos.y + forward.y)?.getOrNull(pos.x + forward.x)
            if (forwardCost != null) {
                states += State(pos + forward, dir, movesInDir + 1, forwardCost)
            }
        }
    }
}

data class State(val pos: Vec2i, val dir: Direction4, val movesInDir: Int, val cost: Int)

private fun solve(grid: List<List<Int>>, nextStates: State.(states: MutableList<State>) -> Unit): Int? {
    val target = v(grid[0].size - 1, grid.size - 1)
    val start = State(v(0, 0), Direction4.RIGHT, 0, 0)

    data class Key(val pos: Vec2i, val dir: Direction4, val moves: Int)

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

    error("No path found")
}

fun main() = PuzzleInput(2023, 17).withSolutions({ part1() }, { part2() }).run()