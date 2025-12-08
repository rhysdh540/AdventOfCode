@file:OptIn(ExperimentalTypeInference::class)
@file:Suppress("NOTHING_TO_INLINE", "unused")

package dev.rdh.aoc

import java.util.BitSet
import kotlin.experimental.ExperimentalTypeInference

typealias Iterable2d<T> = Iterable<Iterable<T>>
typealias List2d<T> = List<List<T>>
typealias MutableList2d<T> = MutableList<MutableList<T>>

fun <T> Iterable2d<T>.rotateClockwise(): List2d<T> {
    return List(this.first().count()) { i ->
        List(this.count()) { j ->
            this.elementAt(this.count() - 1 - j).elementAt(i)
        }
    }
}

fun <T> Iterable2d<T>.rotateCounterClockwise(): List2d<T> {
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

fun <T> Iterable2d<T>.deepToMutableList(): MutableList2d<T> {
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

inline operator fun <T> List<T>.get(range: IntRange): List<T> {
    return this.subList(range.first, range.last)
}

@JvmName("getMutable")
inline operator fun <T> MutableList<T>.get(range: IntRange): MutableList<T> {
    return (this as List<T>)[range] as MutableList<T>
}

inline operator fun <T> MutableList<T>.set(range: IntRange, newList: Iterable<T>) {
    this.subList(range.first, range.last)
        .clear()
    this.addAll(range.first, newList.toList())
}

fun Iterable<Boolean>.cardinality(): Int {
    var count = 0
    for (b in this) {
        if (b) count++
    }
    return count
}

fun BooleanArray.cardinality(): Int {
    var count = 0
    for (b in this) {
        if (b) count++
    }
    return count
}

fun Iterable<Boolean>.anyTrue(): Boolean {
    for (b in this) {
        if (b) return true
    }
    return false
}

fun BooleanArray.anyTrue(): Boolean {
    for (b in this) {
        if (b) return true
    }
    return false
}

fun Iterable<Boolean>.anyFalse(): Boolean {
    for (b in this) {
        if (!b) return true
    }
    return false
}

fun BooleanArray.anyFalse(): Boolean {
    for (b in this) {
        if (!b) return true
    }
    return false
}

fun Iterable<Boolean>.allTrue() = !anyFalse()
fun BooleanArray.allTrue() = !anyFalse()
fun Iterable<Boolean>.allFalse() = !anyTrue()
fun BooleanArray.allFalse() = !anyTrue()

fun <T> Iterable<T>.permutations(): List<Iterable<T>> {
    if (count() == 1) return listOf(this)
    val perms = mutableListOf<List<T>>()
    val toInsert = first()
    for (perm in drop(1).permutations()) {
        for (i in 0..perm.count()) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, toInsert)
            perms.add(newPerm)
        }
    }
    return perms
}

fun <T> Iterable<T>.toPair(forceTwo: Boolean = false): Pair<T, T> {
    val iter = this.iterator()
    val first = iter.next()
    val second = iter.next()
    if (forceTwo && iter.hasNext()) {
        throw IllegalArgumentException("Iterable has more than two elements")
    }
    return Pair(first, second)
}

@JvmName("toVec2i")
fun Iterable<Int>.toVec2(): Vec2i {
    val (x, y) = this.toPair(true)
    return Vec2i(x, y)
}

@JvmName("toVec2l")
fun Iterable<Long>.toVec2(): Vec2l {
    val (x, y) = this.toPair(true)
    return Vec2l(x, y)
}

@JvmName("toVec2d")
fun Iterable<Double>.toVec2(): Vec2d {
    val (x, y) = this.toPair(true)
    return Vec2d(x, y)
}

@JvmName("toVec3i")
fun Iterable<Int>.toVec3(): Vec3i {
    val (x, y, z) = this.toTriple(true)
    return Vec3i(x, y, z)
}

@JvmName("toVec3l")
fun Iterable<Long>.toVec3(): Vec3l {
    val (x, y, z) = this.toTriple(true)
    return Vec3l(x, y, z)
}

@JvmName("toVec3l")
fun Iterable<Double>.toVec3(): Vec3d {
    val (x, y, z) = this.toTriple(true)
    return Vec3d(x, y, z)
}

fun <T> Iterable<T>.toTriple(forceThree: Boolean = false): Triple<T, T, T> {
    val iter = this.iterator()
    val first = iter.next()
    val second = iter.next()
    val third = iter.next()
    if (forceThree && iter.hasNext()) {
        throw IllegalArgumentException("Iterable has more than three elements")
    }
    return Triple(first, second, third)
}

inline fun <T> Iterable<T>.sumIndexed(transform: (index: Int, T) -> Int): Int {
    var index = 0
    var sum = 0
    for (element in this) {
        sum += transform(index, element)
        index++
    }
    return sum
}

fun IntRange.intersect(other: IntRange): IntRange? {
    val s = maxOf(first, other.first)
    val e = minOf(last, other.last)
    return if (s <= e) s..e else null
}

fun CharSequence.count(char: Char): Int {
    var count = 0
    for (c in this) {
        if (c == char) count++
    }
    return count
}

fun BitSet.toBooleanArray(): BooleanArray {
    val array = BooleanArray(this.length())
    var i = this.nextSetBit(0)
    while (i >= 0) {
        array[i] = true
        i = this.nextSetBit(i + 1)
    }
    return array
}

inline fun BitSet.forEachSet(action: (Int) -> Unit) {
    var i = this.nextSetBit(0)
    while (i >= 0) {
        action(i)
        i = this.nextSetBit(i + 1)
    }
}

inline fun BitSet.map(transform: (Int) -> Int): List<Int> {
    val result = mutableListOf<Int>()
    var i = this.nextSetBit(0)
    while (i >= 0) {
        result.add(transform(i))
        i = this.nextSetBit(i + 1)
    }
    return result
}

fun BitSet.toIntArray(): IntArray {
    val arr = IntArray(this.cardinality())
    var index = 0
    var i = this.nextSetBit(0)
    while (i >= 0) {
        arr[index++] = i
        i = this.nextSetBit(i + 1)
    }
    return arr
}

fun Iterable<Int>.product(): Int {
    var prod = 1
    for (v in this) {
        prod *= v
    }
    return prod
}

fun Iterable<Long>.product(): Long {
    var prod = 1L
    for (v in this) {
        prod *= v
    }
    return prod
}

fun Iterable<Double>.product(): Double {
    var prod = 1.0
    for (v in this) {
        prod *= v
    }
    return prod
}

fun IntArray.product(): Int {
    var prod = 1
    for (v in this) {
        prod *= v
    }
    return prod
}

fun LongArray.product(): Long {
    var prod = 1L
    for (v in this) {
        prod *= v
    }
    return prod
}

fun DoubleArray.product(): Double {
    var prod = 1.0
    for (v in this) {
        prod *= v
    }
    return prod
}
