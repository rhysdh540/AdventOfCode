import dev.rdh.aoc.*
import Type.*

private fun PuzzleInput.part1(): Any? {
    val (grid, start) = getGrid()
    return grid.getLoop(start).size / 2
}

private fun PuzzleInput.part2(): Any? {
    val (grid, start) = getGrid()
    val loop = grid.getLoop(start)

    return grid.sumOf { row -> row.count { part -> !loop.contains(part) && !grid.hasPathToEdge(part, loop) } }
}

private fun PuzzleInput.getGrid(): Pair<Array<Array<PipePart>>, PipePart> {
    val grid: Array<Array<PipePart>> = lines.mapIndexed { y, line ->
        line.mapIndexed { x, c -> PipePart(Type.of(c), x, y) }.toTypedArray()
    }.toTypedArray()

    val start = grid.flatten().find { it.type == START }!!

    return Pair(grid, start)
}

private fun Array<Array<PipePart>>.getLoop(start: PipePart): MutableSet<PipePart> {
    var start = start
    var x = start.x
    var y = start.y
    var prevX = x
    var prevY = y
    var length = 0

    val loop = mutableSetOf(start)

    start = determineType(start, this)
    this[y][x] = start

    while (true) {
        val part = this[y][x]
        if (part.type == EMPTY || (part == start && length > 0)) {
            break
        }

        length++
        loop.add(part)
        val adjacent = part.findAdjacent(this)
        var next = adjacent[0]
        if (next.x == prevX && next.y == prevY) {
            next = adjacent[1]
        }

        prevX = x
        prevY = y
        x = next.x
        y = next.y
    }
    return loop
}

private fun Array<Array<PipePart>>.hasPathToEdge(part: PipePart, loop: Set<PipePart>): Boolean {
    return (part.x until this[part.y].size).count {
        this[part.y][it].type.hasNorth() && loop.contains(this[part.y][it])
    } % 2 == 0
}

private fun determineType(start: PipePart, grid: Array<Array<PipePart>>): PipePart {
    val adjacent = start.findAdjacent(grid)
    var north = false
    var south = false
    var east = false
    var west = false
    for (adj in adjacent) {
        if (adj.y < start.y) north = true
        if (adj.y > start.y) south = true
        if (adj.x < start.x) west = true
        if (adj.x > start.x) east = true
    }

    val type = when {
        north && south && !east && !west -> VERTICAL
        !north && !south && east && west -> HORIZONTAL
        north && !south && east && !west -> NORTH_EAST
        north && !south && !east && west -> NORTH_WEST
        !north && south && east && !west -> SOUTH_EAST
        !north && south && !east && west -> SOUTH_WEST
        else -> throw AssertionError()
    }

    return PipePart(type, start.x, start.y)
}

private data class PipePart(val type: Type, val x: Int, val y: Int) {
    fun findAdjacent(grid: Array<Array<PipePart>>): MutableList<PipePart> {
        val result = mutableListOf<PipePart>()

        if (y > 0) {
            val north = grid[y - 1][x]
            if (type.hasNorth() && north.type.hasSouth()) {
                result.add(north)
            }
        }

        if (y < grid.size - 1) {
            val south = grid[y + 1][x]
            if (type.hasSouth() && south.type.hasNorth()) {
                result.add(south)
            }
        }

        if (x < grid[y].size - 1) {
            val east = grid[y][x + 1]
            if (type.hasEast() && east.type.hasWest()) {
                result.add(east)
            }
        }

        if (x > 0) {
            val west = grid[y][x - 1]
            if (type.hasWest() && west.type.hasEast()) {
                result.add(west)
            }
        }

        return result
    }

    override fun equals(other: Any?): Boolean {
        return other is PipePart && other.x == x && other.y == y
    }

    override fun hashCode(): Int {
        return x * 31 + y
    }
}

private enum class Type(private val t: Char) {
    START('S'),
    VERTICAL('|'),
    HORIZONTAL('-'),
    NORTH_EAST('L'),
    NORTH_WEST('J'),
    SOUTH_EAST('F'),
    SOUTH_WEST('7'),
    EMPTY('.'),
    ;

    fun hasNorth(): Boolean {
        return this == VERTICAL || this == NORTH_EAST || this == NORTH_WEST || this == START
    }

    fun hasSouth(): Boolean {
        return this == VERTICAL || this == SOUTH_EAST || this == SOUTH_WEST || this == START
    }

    fun hasEast(): Boolean {
        return this == HORIZONTAL || this == NORTH_EAST || this == SOUTH_EAST || this == START
    }

    fun hasWest(): Boolean {
        return this == HORIZONTAL || this == NORTH_WEST || this == SOUTH_WEST || this == START
    }

    companion object {
        fun of(t: Char): Type {
            return entries.first { it.t == t }
        }
    }
}

fun main() = PuzzleInput(2023, 10).withSolutions({ part1() }, { part2() }).run()