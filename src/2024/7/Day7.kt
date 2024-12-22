import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.log10
import kotlin.math.pow

fun part1_7(input: String): Any? {
    val equations = input.lines().map {
        val (l, r) = it.split(": ")
        val result = l.toLong()
        val nums = r.split(" ").map { it.toInt() }
        Pair(result, nums)
    }

    fun go(nums: List<Int>, target: Long, index: Int = 1, sum: Long = nums[0].toLong()): Boolean {
        if (index == nums.size) {
            return sum == target
        }

        if(go(nums, target, index + 1, sum + nums[index])) {
            return true
        }

        if(go(nums, target, index + 1, sum * nums[index])) {
            return true
        }

        return false
    }

    var sum = 0L
    for((result, nums) in equations) {
        if(go(nums, result)) {
            sum += result
        }
    }

    return sum
}

fun part2_7(input: String): Any? {
    val equations = input.lines().map {
        val (l, r) = it.split(": ")
        val result = l.toLong()
        val nums = r.split(" ").map { it.toInt() }
        Pair(result, nums)
    }

    fun Long.concat(b: Int): Long {
        val bLength = log10(b.toDouble()).toInt() + 1
        val newA = this * 10.0.pow(bLength) + b
        return newA.toLong()
    }

    fun go(nums: List<Int>, target: Long, index: Int = 1, sum: Long = nums[0].toLong()): Boolean {
        if (index == nums.size) {
            return sum == target
        }

        if(go(nums, target, index + 1, sum + nums[index])) {
            return true
        }

        if(go(nums, target, index + 1, sum * nums[index])) {
            return true
        }

        if(go(nums, target, index + 1, sum.concat(nums[index]))) {
            return true
        }

        return false
    }

    return equations.filter { (result, nums) -> go(nums, result) }.sumOf { it.first }
}

fun main() {
    val input = Path("inputs/2024/7.txt").readText()

    var start = System.nanoTime()
    var result = part1_7(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_7(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}