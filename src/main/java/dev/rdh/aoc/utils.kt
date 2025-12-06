@file:Suppress("NOTHING_TO_INLINE", "unused")

package dev.rdh.aoc

val Iterable<String>.ints get() = map { it.toInt() }
val Iterable<String>.longs get() = map { it.toLong() }
val String.spaced get() = split(' ').filter { it.isNotEmpty() }
const val blankLines = "\n\n"
