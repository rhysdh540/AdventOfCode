package dev.rdh.aoc

val Iterable<String>.ints get() = map { it.toInt() }
val Iterable<String>.longs get() = map { it.toLong() }
val String.spaced get() = split(' ')
const val blankLines = "\n\n"
