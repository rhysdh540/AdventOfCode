@file:Suppress("UNCHECKED_CAST", "EXTENSION_SHADOWED_BY_MEMBER")

import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val inputs = lines.map { line ->
        val parts = line.split(' ')
        val pattern = parts[0].let {
            it.substring(1, it.length - 1).map { it == '#' }
        }
        val reqs = parts.last().let {
            it.substring(1, it.length - 1).split(',').ints
        }
        val buttons = parts.subList(1, parts.size - 1).map {
            it.substring(1, it.length - 1).split(',').ints
        }
        Triple(pattern, buttons, reqs)
    }

    val field = object : NumericField<Boolean, BooleanArray>(false, true) {
        override fun Boolean.plus(b: Boolean): Boolean = this xor b
        override fun Boolean.minus(b: Boolean): Boolean = this xor b
        override fun Boolean.times(b: Boolean): Boolean = this && b
        override fun Boolean.div(b: Boolean): Boolean = if (b) this else error("division by zero in GF(2)")
        override fun abs(n: Boolean): Boolean = n
        override fun Boolean.eq(o: Boolean): Boolean = this == o
        
        override fun Boolean.toInt(): Int = if (this) 1 else 0
        override fun fromInt(k: Int): Boolean = (k % 2) != 0

        override fun newarr(vararg n: Int): Any = java.lang.reflect.Array.newInstance(Boolean::class.java, *n)
        override fun BooleanArray.get(i: Int): Boolean = this[i]
        override fun BooleanArray.set(i: Int, value: Boolean) { this[i] = value }
        override val BooleanArray.size: Int get() = this.size
    }

    return inputs.sumOf { (pattern, buttons, _) ->
        field.minimize(
            field.makeSystem(
                buttons,
                pattern
            ),
            IntArray(buttons.size) { 1 } // doesn't really make sense to push a button more than once
        )
    }
}

private fun PuzzleInput.part2(): Any? {
    val inputs = lines.map { line ->
        val parts = line.split(' ')
        val pattern = parts[0].let {
            it.substring(1, it.length - 1).map { ch -> ch == '#' }
        }
        val reqs = parts.last().let {
            it.substring(1, it.length - 1).split(',').ints
        }
        val buttons = parts.subList(1, parts.size - 1).map {
            it.substring(1, it.length - 1).split(',').ints
        }
        Triple(pattern, buttons, reqs)
    }

    val field = object : NumericField<Double, DoubleArray>(0.0, 1.0) {

        override fun Double.plus(b: Double): Double = this + b
        override fun Double.minus(b: Double): Double = this - b
        override fun Double.times(b: Double): Double = this * b
        override fun Double.div(b: Double): Double = this / b
        override fun abs(n: Double): Double = kotlin.math.abs(n)
        override fun Double.eq(o: Double): Boolean = abs(this - o) < 1e-9

        override fun Double.toInt(): Int = kotlin.math.round(this).toInt()
        override fun fromInt(k: Int): Double = k.toDouble()

        override fun newarr(vararg n: Int): Any = java.lang.reflect.Array.newInstance(Double::class.java, *n)
        override fun DoubleArray.get(i: Int): Double = this[i]
        override fun DoubleArray.set(i: Int, value: Double) { this[i] = value }
        override val DoubleArray.size: Int get() = this.size
    }

    return inputs.sumOf { (_, buttons, reqs) ->
        field.minimize(
            field.makeSystem(
                buttons,
                reqs.map { it.toDouble() }
            ),
            buttons.map { b -> b.minOf { reqs[it] } }.toIntArray()
        )
    }
}

private abstract class NumericField<N : Comparable<N>, V>(val zero: N, val one: N) {
    abstract operator fun N.plus(b: N): N
    abstract operator fun N.minus(b: N): N
    abstract operator fun N.times(b: N): N
    abstract operator fun N.div(b: N): N
    abstract fun abs(n: N): N
    abstract infix fun N.eq(o: N): Boolean

    abstract fun N.toInt(): Int
    abstract fun fromInt(k: Int): N

    abstract fun newarr(vararg n: Int): Any
    abstract operator fun V.get(i: Int): N
    abstract operator fun V.set(i: Int, value: N)
    abstract val V.size: Int
}

@Suppress("ArrayInDataClass") // shush im just here for free destructuring
private data class LinearSystem<N : Comparable<N>, V>(
    // the augmented matrix [A | t]
    val mat: Array<V>,
    // row index that has a pivot (leading 1) for each column, or -1 if free
    val pivots: IntArray,
    // indices of free columns (with no pivot)
    val freeCols: IntArray
)

private fun <N : Comparable<N>, V> NumericField<N, V>.makeSystem(
    values: List<List<Int>>,
    target: List<N>
): LinearSystem<N, V> {
    val m = target.size
    val n = values.size
    val mat = newarr(m, n + 1) as Array<V>

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

// convert the augmented matrix to row echelon form, returning the columns that have pivots
context(f: NumericField<N, V>)
private fun <N : Comparable<N>, V> ref(mat: Array<V>): IntArray = with(f) {
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

// given a linear system in REF form, and values for the free variables,
// reconstruct the full solution vector
context(f: NumericField<N, V>)
private fun <N : Comparable<N>, V> reconstructSolution(system: LinearSystem<N, V>, freeValues: V): V = with(f) {
    val (mat, pivots, freeCols) = system
    val n = pivots.size
    val x = newarr(n) as V

    // fill in free variables in solution
    for (i in 0 until freeCols.size) {
        x[freeCols[i]] = freeValues[i]
    }

    val lastCol = mat[0].size - 1

    // for each variable with a pivot, compute its value from the rest of the row
    // the row has the form: x_pivot + sum(coeff_i * x_free_i) = rhs
    // so x_pivot = rhs - sum(coeff_i * x_free_i)
    for (col in n - 1 downTo 0) {
        val r = pivots[col] // row for this pivot column
        if (r == -1) continue // if free variable, skip

        var value = mat[r][lastCol] // rhs

        // subtract (coeff * x_free) for each free variable in this row
        for (c in col + 1 until n) {
            val coeff = mat[r][c]
            if (!(coeff eq zero)) {
                value -= coeff * x[c]
            }
        }
        x[col] = value
    }

    return x
}

context(f: NumericField<N, V>)
private fun <N : Comparable<N>, V> validateAndSum(x: V, maxPress: IntArray): Int? = with(f) {
    var sumPress = 0
    val n = x.size
    for (col in 0 until n) {
        val v = x[col]
        if (v eq zero) continue
        if (v < zero) return null // negative
        val intV = v.toInt()
        if (!(v eq fromInt(intV))) return null // not integer
        if (intV > maxPress[col]) return null // exceeds max
        sumPress += intV
    }
    return sumPress
}

private fun <N : Comparable<N>, V> NumericField<N, V>.minimize(system: LinearSystem<N, V>, maxPress: IntArray): Int {
    var best: Int? = null

    fun dfs(idx: Int, freeValues: V, currentSum: Int = 0) {
        if (best != null && currentSum >= best!!) return // prune search if already worse than best
        if (idx == system.freeCols.size) {
            val x = reconstructSolution(system, freeValues)
            validateAndSum(x, maxPress)?.let {
                if (best == null || it < best!!) {
                    best = it
                }
            }
            return
        }

        // literally just try every possible combination of free variable assignments
        for (press in 0..maxPress[system.freeCols[idx]]) {
            freeValues[idx] = fromInt(press)
            dfs(idx + 1, freeValues, currentSum + press)
        }
    }

    dfs(0, newarr(system.freeCols.size) as V)

    return best ?: error("no valid solution for line, uh oh!")
}

fun main() = PuzzleInput(2025, 10).withSolutions({ part1() }, { part2() }).run()