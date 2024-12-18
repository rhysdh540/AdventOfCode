import kotlin.io.path.Path
import kotlin.io.path.readText
import Day15.sumIndexed
import Day15.set
import Day15.plus
import Day15.get

fun part1_15(input: String): Any? {
    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(first + other.first, second + other.second)
    operator fun <T> List<List<T>>.get(pos: Pair<Int, Int>) = this[pos.second][pos.first]
    operator fun <T> MutableList<MutableList<T>>.set(pos: Pair<Int, Int>, value: T) { this[pos.second][pos.first] = value }

    val (gridStr, insnsStr) = input.split("\n\n")
    val grid = gridStr.lines().map { it.toMutableList() }.toMutableList()
    val insns = insnsStr.replace("\n", "")

    var robot = grid.mapIndexed { y, row -> row.mapIndexed { x, c -> if (c == '@') Pair(x, y) else null } }.flatten().filterNotNull().first()
    grid[robot] = '.'
    val dirs = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))

    fun canMoveBoxes(start: Pair<Int, Int>, dir: Pair<Int, Int>): Boolean {
        val next = start + dir
        if (grid[next] == '#') return false
        if (grid[next] == 'O') return canMoveBoxes(next, dir)
        return true
    }

    fun moveBoxes(start: Pair<Int, Int>, dir: Pair<Int, Int>) {
        val next = start + dir
        if (grid[next] == 'O') moveBoxes(next, dir)
        grid[next] = 'O'
        grid[start] = '.'
    }

    for (insn in insns) {
        val dir = when (insn) {
            '^' -> dirs[0]
            '>' -> dirs[1]
            'v' -> dirs[2]
            '<' -> dirs[3]
            else -> error("Invalid instruction: $insn")
        }
        val next = robot + dir

        if (grid[next] != '#') {
            if (grid[next] == 'O') {
                if (canMoveBoxes(next, dir)) {
                    moveBoxes(next, dir)
                    robot = next
                }
            } else {
                robot = next
            }
        }
    }

    return grid.sumIndexed { y, row ->
        row.sumIndexed { x, c ->
            if (c == 'O') 100 * y + x else 0
        }
    }
}

fun part2_15(input: String): Any? {
    val (gridStr, insnsStr) = input.split("\n\n")
    val grid = gridStr.lines().map {
        it.flatMap {
            when (it) {
                '#' -> "##"
                'O' -> "[]"
                '@' -> "@."
                '.' -> ".."
                else -> error("Invalid character: $it")
            }.toList()
        }.toMutableList()
    }.toMutableList()
    val insns = insnsStr.replace("\n", "")

    var robot = grid.mapIndexed { y, row -> row.mapIndexed { x, c -> if (c == '@') Pair(x, y) else null } }.flatten().filterNotNull().first()
    grid[robot] = '.'

    val dirs = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))

    fun canMoveBoxes(start: Pair<Int, Int>, dir: Pair<Int, Int>): Boolean {
        val (left, right) = when (grid[start]) {
            '[' -> Pair(start, start + dirs[1])
            ']' -> Pair(start + dirs[3], start)
            else -> error("Not a box cell")
        }

        val rNext = right + dir
        val lNext = left + dir

        if(grid[rNext] == '#' || grid[lNext] == '#') return false
        if(grid[rNext] in "[]" && rNext != left && !canMoveBoxes(rNext, dir)) return false
        if(grid[lNext] in "[]" && lNext != right && !canMoveBoxes(lNext, dir)) return false
        return true
    }

    fun moveBoxes(start: Pair<Int, Int>, dir: Pair<Int, Int>) {
        val (left, right) = when (grid[start]) {
            '[' -> Pair(start, start + dirs[1])
            ']' -> Pair(start + dirs[3], start)
            else -> error("Not a box cell")
        }

        val rNext = right + dir
        val lNext = left + dir

        if(grid[rNext] in "[]" && rNext != left) moveBoxes(rNext, dir)
        if(grid[lNext] in "[]" && lNext != right) moveBoxes(lNext, dir)

        grid[right] = '.'
        grid[left] = '.'
        grid[rNext] = ']'
        grid[lNext] = '['
    }

    for (insn in insns) {
        val dir = when (insn) {
            '^' -> dirs[0]
            '>' -> dirs[1]
            'v' -> dirs[2]
            '<' -> dirs[3]
            else -> error("Invalid instruction: $insn")
        }
        val next = robot + dir

        if (grid[next] != '#') {
            if (grid[next] in "[]") {
                if (canMoveBoxes(next, dir)) {
                    moveBoxes(next, dir)
                    robot = next
                }
            } else {
                robot = next
            }
        }
    }

    return grid.sumIndexed { y, row ->
        row.sumIndexed { x, c ->
            if (c == '[') 100 * y + x else 0
        }
    }
}

object Day15 {
    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(first + other.first, second + other.second)
    operator fun <T> List<List<T>>.get(pos: Pair<Int, Int>) = this[pos.second][pos.first]
    operator fun <T> MutableList<MutableList<T>>.set(pos: Pair<Int, Int>, value: T) { this[pos.second][pos.first] = value }
    fun <T> Iterable<T>.sumIndexed(transform: (index: Int, T) -> Int) = mapIndexed(transform).sum()
    fun pg(grid: List<List<Char>>, robot: Pair<Int, Int>) =
        println(grid.mapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (Pair(x, y) == robot) '@' else c
            }.joinToString("")
        }.joinToString("\n") { it })
}

fun main() {
    val input = Path("inputs/2024/15.txt").readText()

    var start = System.nanoTime()
    var result = part1_15(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_15(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}