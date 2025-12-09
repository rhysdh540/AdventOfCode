import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return solve(3)
}

private fun PuzzleInput.part2(): Any? {
    return solve(4)
}

private fun PuzzleInput.solve(groups: Int): Long {
    val weights = lines.ints
    val targetWeight = weights.sum() / groups
    var minSize = Int.MAX_VALUE
    var minQuantumEntanglement = Long.MAX_VALUE

    fun findCombinations(nums: List<Int>, target: Int, start: Int = 0, current: List<Int> = emptyList()) {
        if (target == 0) {
            // found a combination
            if (current.size < minSize) {
                minSize = current.size
                minQuantumEntanglement = current.product()
            } else if (current.size == minSize) {
                minQuantumEntanglement = minOf(minQuantumEntanglement, current.product())
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

    findCombinations(weights, targetWeight)
    return minQuantumEntanglement
}

fun main() = PuzzleInput(2015, 24).withSolutions({ part1() }, { part2() }).run()