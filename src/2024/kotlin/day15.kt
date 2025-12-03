import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(first + other.first, second + other.second)
    operator fun <T> List<List<T>>.get(pos: Pair<Int, Int>) = this[pos.second][pos.first]
    operator fun <T> MutableList<MutableList<T>>.set(pos: Pair<Int, Int>, value: T) {
        this[pos.second][pos.first] = value
    }

    val (gridStr, insnsStr) = sections
    val grid = gridStr.lines().map { it.toMutableList() }.toMutableList()
    val insns = insnsStr.replace("\n", "")

    var robot = grid.mapIndexed { y, row -> row.mapIndexed { x, c -> if (c == '@') Pair(x, y) else null } }.flatten()
        .filterNotNull().first()
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

private fun PuzzleInput.part2(): Any? {
    val (gridStr, insnsStr) = sections
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

    var robot = grid.mapIndexed { y, row -> row.mapIndexed { x, c -> if (c == '@') Pair(x, y) else null } }.flatten()
        .filterNotNull().first()
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

        if (grid[rNext] == '#' || grid[lNext] == '#') return false
        if (grid[rNext] in "[]" && rNext != left && !canMoveBoxes(rNext, dir)) return false
        if (grid[lNext] in "[]" && lNext != right && !canMoveBoxes(lNext, dir)) return false
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

        if (grid[rNext] in "[]" && rNext != left) moveBoxes(rNext, dir)
        if (grid[lNext] in "[]" && lNext != right) moveBoxes(lNext, dir)

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

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(first + other.first, second + other.second)
private operator fun <T> List<List<T>>.get(pos: Pair<Int, Int>) = this[pos.second][pos.first]
private operator fun <T> MutableList<MutableList<T>>.set(pos: Pair<Int, Int>, value: T) {
    this[pos.second][pos.first] = value
}

private fun <T> Iterable<T>.sumIndexed(transform: (index: Int, T) -> Int) = mapIndexed(transform).sum()
private fun pg(grid: List<List<Char>>, robot: Pair<Int, Int>) =
    println(grid.mapIndexed { y, row ->
        row.mapIndexed { x, c ->
            if (Pair(x, y) == robot) '@' else c
        }.joinToString("")
    }.joinToString("\n") { it })

fun main() = PuzzleInput(2024, 15).withSolutions({ part1() }, { part2() }).run()