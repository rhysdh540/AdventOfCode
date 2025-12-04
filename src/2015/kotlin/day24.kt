import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return lines.ints.let {
        run(it, it.sum() / 3)
    }
}

private fun PuzzleInput.part2(): Any? {
    return lines.ints.let {
        run(it, it.sum() / 4)
    }
}

private fun PuzzleInput.run(nums: List<Int>, target: Int): Long {
    var minSize = Int.MAX_VALUE
    var minQuantumEntanglement = Long.MAX_VALUE

    fun findCombinations(nums: List<Int>, target: Int, start: Int = 0, current: List<Int> = emptyList()) {
        if (target == 0) {
            // found a combination
            if (current.size < minSize) {
                minSize = current.size
                minQuantumEntanglement = current.fold(1L) { acc, i -> acc * i }
            } else if (current.size == minSize) {
                minQuantumEntanglement = minOf(minQuantumEntanglement, current.fold(1L) { acc, i -> acc * i })
            }
        } else if (target < 0 || start >= nums.size || current.size > minSize) {
            // stop searching if the target is negative or too large
            return
        } else {
            for (i in start until nums.size) {
                findCombinations(nums, target - nums[i], i + 1, current + nums[i])
            }
        }
    }

    findCombinations(nums, target)
    return minQuantumEntanglement
}

fun main() = PuzzleInput(2015, 24).withSolutions({ part1() }, { part2() }).run()