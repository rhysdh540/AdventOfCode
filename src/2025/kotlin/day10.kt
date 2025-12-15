@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER", "ArrayInDataClass")

import dev.rdh.aoc.*
import kotlin.time.Duration.Companion.seconds

private fun PuzzleInput.part1(): Any? {
    val field = object : NumericField<Boolean>(false, true) {
        override fun Boolean.plus(b: Boolean): Boolean = this xor b
        override fun Boolean.minus(b: Boolean): Boolean = this xor b
        override fun Boolean.times(b: Boolean): Boolean = this && b
        override fun Boolean.div(b: Boolean): Boolean = if (b) this else error("division by zero in GF(2)")
        override fun Boolean.compareTo(b: Boolean) = this.compareTo(b)
        override fun abs(n: Boolean): Boolean = n
        override fun Boolean.eq(o: Boolean): Boolean = this == o

        override fun Boolean.toInt(): Int = if (this) 1 else 0
        override fun fromInt(k: Int): Boolean = (k and 1) == 1
        override fun Boolean.isExactInt(): Boolean = true
    }

    return lines.parallelStream().mapToInt { line ->
        val parts = line.split(' ')
        val pattern = parts[0].let {
            it.substring(1, it.length - 1).map { it == '#' }
        }
        val buttons = parts.subList(1, parts.size - 1).map {
            it.substring(1, it.length - 1).split(',').ints
        }

        field.minimize(
            field.makeSystem(
                buttons,
                pattern
            ),
            IntArray(buttons.size) { 1 } // doesn't really make sense to push a button more than once
        )
    }.sum()
}

private fun PuzzleInput.part2(): Any? {
    val field = object : NumericField<Double>(0.0, 1.0) {
        override fun Double.plus(b: Double): Double = this + b
        override fun Double.minus(b: Double): Double = this - b
        override fun Double.times(b: Double): Double = this * b
        override fun Double.div(b: Double): Double = this / b
        override fun Double.compareTo(b: Double) = this.compareTo(b)
        override fun abs(n: Double): Double = kotlin.math.abs(n)
        override fun Double.eq(o: Double): Boolean = abs(this - o) < 1e-9

        override fun Double.toInt(): Int = kotlin.math.round(this).toInt()
        override fun fromInt(k: Int): Double = k.toDouble()
        override fun Double.isExactInt(): Boolean = this eq kotlin.math.round(this)
    }

    return lines.parallelStream().mapToInt { line ->
        val parts = line.split(' ')
        val reqs = parts.last().let {
            it.substring(1, it.length - 1).split(',').ints
        }
        val buttons = parts.subList(1, parts.size - 1).map {
            it.substring(1, it.length - 1).split(',').ints
        }

        field.minimize(
            field.makeSystem(
                buttons,
                reqs.map { field.fromInt(it) }
            ),
            IntArray(buttons.size) { i -> buttons[i].minOf { reqs[it] } }
        )
    }.sum()
}

private abstract class NumericField<N>(val zero: N, val one: N) {
    abstract operator fun N.plus(b: N): N
    abstract operator fun N.minus(b: N): N
    abstract operator fun N.times(b: N): N
    abstract operator fun N.div(b: N): N
    abstract operator fun N.compareTo(b: N): Int
    abstract infix fun N.eq(o: N): Boolean
    abstract fun abs(n: N): N

    abstract fun N.toInt(): Int
    abstract fun fromInt(k: Int): N
    abstract fun N.isExactInt(): Boolean
}

private data class LinearSystem<N>(
    // the augmented matrix [A | t]
    val mat: Array<Array<N>>,
    // row index that has a pivot (leading 1) for each column, or -1 if free
    val pivots: IntArray,
    // indices of free columns (with no pivot)
    val freeCols: IntArray
)

private fun <N> NumericField<N>.makeSystem(
    values: List<List<Int>>,
    target: List<N>
): LinearSystem<N> {
    val m = target.size
    val n = values.size
    @Suppress("UNCHECKED_CAST")
    val mat = Array<Array<Any?>>(m) { Array(n + 1) { zero } } as Array<Array<N>>

    // fill matrix
    for (slot in 0 until m) {
        for (btnIdx in 0 until n) {
            if (slot in values[btnIdx]) {
                mat[slot][btnIdx] = one
            }
        }
        mat[slot][n] = target[slot]
    }

    val pivots = ref(mat)

    // free columns are those without pivots
    val freeCols = pivots.indices.filter { pivots[it] == -1 }.toIntArray()

    return LinearSystem(mat, pivots, freeCols)
}

// convert the augmented matrix to an upper triangular row echelon form, returning the columns that have pivots
context(f: NumericField<N>)
private fun <N> ref(mat: Array<Array<N>>): IntArray = with(f) {
    val m = mat.size
    val n = mat[0].size - 1 // augmented, so last column is rhs
    val pivots = IntArray(n) { -1 }
    var row = 0

    for (col in 0 until n) {
        // find the row to use as pivot (any non-zero will work)
        val pivotRow = (row until m).firstOrNull { r -> !(mat[r][col] eq zero) } ?: continue

        // swap best row into position
        if (pivotRow != row) {
            val tmp = mat[row]
            mat[row] = mat[pivotRow]
            mat[pivotRow] = tmp
        }

        // normalize pivot row, so pivot element is 1
        val pivot = mat[row][col]
        if (!(pivot eq one)) {
            for (c in col until n + 1) {
                mat[row][c] /= pivot
            }
        }

        // eliminate this column from all other rows below, creating an upper triangular matrix
        for (r in (row + 1) until m) {
            if (r == row) continue
            val factor = mat[r][col]
            if (factor eq zero) continue
            for (c in col until n + 1) {
                mat[r][c] -= factor * mat[row][c]
            }
        }

        // record that col has a pivot in this row
        pivots[col] = row
        row++
        // no more rows to process
        if (row == m) break
    }

    return pivots
}

private fun <N> NumericField<N>.minimize(system: LinearSystem<N>, maxPress: IntArray): Int {
    val (mat, pivots, _) = system
    val n = pivots.size
    val lastCol = mat[0].size - 1

    data class Node(val col: Int, val cost: Int, val x: IntArray)

    val queue = ArrayDeque<Node>()
    queue += Node(col = n - 1, cost = 0, x = IntArray(n))

    var best: Int? = null

    while (queue.isNotEmpty()) {
        val (col, cost, x) = queue.removeFirst()
        if (best != null && cost >= best) continue
        if (col < 0) {
            best = cost
            continue
        }

        if (pivots[col] == -1) {
            // literally just try every possible combination of free variable assignments
            for (press in 0..maxPress[col]) {
                val nx = x.clone()
                nx[col] = press
                queue += Node(col - 1, cost + press, nx)
            }
        } else {
            // pivot variable: compute it immediately from the pivot row
            val r = pivots[col]

            // for each variable with a pivot, compute its value by back substitution
            // the way the matrix was reduced earlier means it's in upper triangular form, so we can go backwards
            // the row has the form: x_pivot + sum_{j > col} (coeff_j * x_j) = rhs
            // so, x_pivot = rhs - sum_{j > col} (coeff_j * x_j)
            // where each x_j is either a free variable (pre-filled) or a pivot variable already solved
            // because we iterate from right-to-left
            var value = mat[r][lastCol] // rhs
            for (c in col + 1 until n) {
                val coeff = mat[r][c]
                if (!(coeff eq zero)) value -= coeff * fromInt(x[c])
            }

            if (!(value eq zero) && value < zero) continue
            if (!value.isExactInt()) continue
            val iv = value.toInt()
            if (iv > maxPress[col]) continue
            val nx = x.clone()
            nx[col] = iv
            queue += Node(col - 1, cost + iv, nx)
        }
    }

    return best ?: error("no valid solution for line, uh oh!")
}

fun main() = PuzzleInput(2025, 10).withSolutions({ part1() }, { part2() }).run()