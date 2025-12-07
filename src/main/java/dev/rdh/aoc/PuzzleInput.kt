@file:Suppress("NOTHING_TO_INLINE", "unused")

package dev.rdh.aoc

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.jvm.isAccessible

class PuzzleInput private constructor(val year: Int, val day: Int, val input: String) {
    constructor(year: Int, day: Int) : this(year, day, Unit.run {
        val callerClass = Class.forName(Thread.currentThread().stackTrace[2].className)
        val resourceName = callerClass.getResource("$day.txt") ?: error("Input file not found for $year/$day")
        resourceName.readText()
    })

    constructor(input: String) : this(-1, -1, input)

    val lines by ResettableLazy { splitBy("\n") }

    fun splitBy(delimiter: String): List<String> {
        return input.split(delimiter)
    }

    val sections by ResettableLazy { splitBy(blankLines) }

    val charGrid: List2d<Char> by ResettableLazy {
        lines.map { it.toList() }
    }

    inline val grid get() = charGrid

    fun boolGrid(trueChar: Char): List2d<Boolean> {
        return charGrid.map { row -> row.map { it == trueChar } }
    }

    val intGrid: List2d<Int> by ResettableLazy {
        charGrid.map { row -> row.map { it.digitToInt() } }
    }

    fun intGridOr(default: Int): List2d<Int> {
        return charGrid.map { row -> row.map { it.digitToIntOrNull() ?: default } }
    }

    val chars: List<Char> by ResettableLazy {
        input.toList()
    }

    internal fun resetLazyProperties() {
        val props = listOf(
            ::lines,
            ::sections,
            ::charGrid,
            ::intGrid,
            ::chars
        )

        for (prop in props) {
            prop.isAccessible = true
            (prop.getDelegate() as ResettableLazy<*>).reset()
        }
    }

    fun withSolutions(part1: PuzzleInput.() -> Any?, part2: PuzzleInput.() -> Any?)
        = Solution(this, part1, part2)
}

private class ResettableLazy<T>(private val initializer: () -> T) : ReadOnlyProperty<Any?, T> {
    private var _value: Any? = UNINITIALIZED_VALUE

    override fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): T {
        if (_value === UNINITIALIZED_VALUE) {
            _value = initializer()
        }
        @Suppress("UNCHECKED_CAST")
        return _value as T
    }

    fun reset() {
        _value = UNINITIALIZED_VALUE
    }

    companion object {
        private val UNINITIALIZED_VALUE = Any()
    }
}

class Solution internal constructor(val input: PuzzleInput,
               private val part1: PuzzleInput.() -> Any?,
               private val part2: PuzzleInput.() -> Any?) {
    fun run() {
        var start = System.nanoTime()
        var result = input.part1()
        var end = System.nanoTime()
        println("Advent of Code ${input.year}/${input.day}")
        println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
        println(result)

        input.resetLazyProperties()

        start = System.nanoTime()
        result = input.part2()
        end = System.nanoTime()
        println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
        println(result)
        println("----------------------")
    }

    fun benchmark(targetTimeMs: Long = 500, maxIterations: Int = 1_000_000, p2TargetTimeMs: Long = targetTimeMs) {
        val p1 = bench(
            name = "Part 1",
            targetTimeNs = targetTimeMs * 1_000_000,
            maxIterations = maxIterations,
            fn = { input.part1() },
            reset = { input.resetLazyProperties() }
        )

        val p2 = bench(
            name = "Part 2",
            targetTimeNs = p2TargetTimeMs * 1_000_000,
            maxIterations = maxIterations,
            fn = { input.part2() },
            reset = { input.resetLazyProperties() }
        )

        println("Benchmark results for ${input.year}/${input.day}:\n    $p1\n    $p2" )
    }
}
