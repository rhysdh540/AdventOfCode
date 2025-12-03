@file:OptIn(ExperimentalTypeInference::class)

package dev.rdh.aoc

import kotlin.experimental.ExperimentalTypeInference

fun <T> Iterable<Iterable<T>>.rotateClockwise(): List<List<T>> {
    return List(this.first().count()) { i ->
        List(this.count()) { j ->
            this.elementAt(this.count() - 1 - j).elementAt(i)
        }
    }
}

fun <T> Iterable<Iterable<T>>.rotateCounterClockwise(): List<List<T>> {
    return List(this.first().count()) { i ->
        List(this.count()) { j ->
            this.elementAt(j).elementAt(this.first().count() - 1 - i)
        }
    }
}

@JvmName("rotateClockwiseString")
fun Iterable<String>.rotateClockwise(): List<String> {
    return List(this.first().length) { i ->
        String(List(this.count()) { j ->
            this.elementAt(this.count() - 1 - j)[i]
        }.toCharArray())
    }
}

@JvmName("rotateCounterClockwiseString")
fun Iterable<String>.rotateCounterClockwise(): List<String> {
    return List(this.first().length) { i ->
        String(List(this.count()) { j ->
            this.elementAt(j)[this.first().length - 1 - i]
        }.toCharArray())
    }
}

fun <T> Iterable<Iterable<T>>.deepToMutableList(): MutableList<MutableList<T>> {
    return map { it.toMutableList() }.toMutableList()
}

@JvmName("sumOfNullableInt")
@OverloadResolutionByLambdaReturnType
inline fun <T> Iterable<T>.sumOf(selector: (T) -> Int?): Int {
    var sum = 0
    for (element in this) {
        sum += selector(element) ?: 0
    }
    return sum
}

@JvmName("sumOfNullableLong")
@OverloadResolutionByLambdaReturnType
inline fun <T> Iterable<T>.sumOf(selector: (T) -> Long?): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element) ?: 0L
    }
    return sum
}