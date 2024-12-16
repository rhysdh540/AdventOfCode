import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_24(input: String): Any? {
    val nums = input.lines().map { it.toInt() }
    return Day24.run(nums, nums.sum() / 3)
}

fun part2_24(input: String): Any? {
    val nums = input.lines().map { it.toInt() }
    return Day24.run(nums, nums.sum() / 4)
}

object Day24 {
    fun run(nums: List<Int>, target: Int): Long {
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
}

fun main() {
    val input = Path("inputs/2015/24.txt").readText()

    var start = System.nanoTime()
    var result = part1_24(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_24(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}