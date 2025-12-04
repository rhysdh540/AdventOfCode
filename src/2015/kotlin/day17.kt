import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val containers = lines.ints
    val target = 150

    // 1 << containers.size is the number of subsets because each container can either be included or not
    return (1 until (1 shl containers.size)).count { i ->
        target == containers.indices.sumOf { j ->
            // If the j-th bit is set, include the j-th container
            if (i and (1 shl j) != 0) {
                containers[j]
            } else {
                0
            }
        }
    }
}

private fun PuzzleInput.part2(): Any? {
    val containers = lines.ints
    val target = 150

    val valid = (1 until (1 shl containers.size)).filter { i ->
        target == containers.indices.sumOf { j ->
            if (i and (1 shl j) != 0) {
                containers[j]
            } else {
                0
            }
        }
    }

    val min = valid.minOf { i ->
        containers.indices.count { j -> (i and (1 shl j) != 0) }
    }

    return valid.count { i ->
        containers.indices.count { j -> (i and (1 shl j) != 0) } == min
    }
}

fun main() = PuzzleInput(2015, 17).withSolutions({ part1() }, { part2() }).run()