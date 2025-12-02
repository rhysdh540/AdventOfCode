import dev.rdh.aoc.*
import kotlin.math.log10
import kotlin.math.pow

private fun PuzzleInput.part1(): Any? {
    val equations = lines.map {
        val (l, r) = it.split(": ")
        val result = l.toLong()
        val nums = r.spaced.ints
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

    return equations.filter { (result, nums) -> go(nums, result) }.sumOf { it.first }
}

private fun PuzzleInput.part2(): Any? {
    val equations = lines.map {
        val (l, r) = it.split(": ")
        val result = l.toLong()
        val nums = r.spaced.ints
        Pair(result, nums)
    }

    infix fun Long.concat(b: Int): Long {
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

        if(go(nums, target, index + 1, sum concat nums[index])) {
            return true
        }

        return false
    }

    return equations.filter { (result, nums) -> go(nums, result) }.sumOf { it.first }
}

fun main() {
    val input = PuzzleInput(2024, 7)

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