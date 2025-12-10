import dev.rdh.aoc.*
import kotlin.math.abs
import kotlin.math.round

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

    return inputs.sumOf { (pattern, buttons, _) ->
        val targetMask = pattern.foldIndexed(0) { idx, acc, on ->
            if (on) acc or (1 shl idx) else acc
        }

        val buttonMasks = buttons.map { btn ->
            btn.fold(0) { acc, lightIdx -> acc or (1 shl lightIdx) }
        }

        var best = Int.MAX_VALUE

        val totalCombos = 1 shl buttons.size
        for (combo in 0 until totalCombos) {
            var state = 0
            var presses = 0

            var mask = combo
            var b = 0
            while (mask != 0) {
                if ((mask and 1) != 0) {
                    state = state xor buttonMasks[b]
                    presses++
                }
                mask = mask ushr 1
                b++
            }

            if (state == targetMask) {
                if (presses < best) {
                    best = presses
                }
            }
        }

        best
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

    return inputs.sumOf { (_, buttons, reqs) ->
        solve(buttons, reqs)
    }
}

private fun solve(buttons: List<List<Int>>, reqs: List<Int>): Int {
    val system = LinearSystem.fromConstraints(buttons, reqs)
    val maxPresses = IntArray(buttons.size) { i ->
        // can't press more than the smallest requirement for any light this button controls
        buttons[i].minOf { reqs[it] }
    }
    return minimize(system, maxPresses)
}

@Suppress("ArrayInDataClass")
private data class LinearSystem(
    // the augmented matrix [A | t]
    val mat: Array<DoubleArray>,
    // row index that has a leading 1 for each column, or -1 if free
    val pivots: IntArray,
    // indices of free columns (with no leading 1)
    val freeCols: IntArray
) {
    companion object {
        fun fromConstraints(buttons: List<List<Int>>, reqs: List<Int>): LinearSystem {
            val m = reqs.size
            val n = buttons.size
            val mat = Array(m) { DoubleArray(n + 1) }

            // fill matrix
            for (slot in 0 until m) {
                for (btnIdx in 0 until n) {
                    if (slot in buttons[btnIdx]) {
                        mat[slot][btnIdx] = 1.0
                    }
                }
                mat[slot][n] = reqs[slot].toDouble()
            }

            val pivots = ref(mat)

            // free columns are those without pivots
            val freeCols = pivots.indices.filter { pivots[it] == -1 }.toIntArray()

            return LinearSystem(mat, pivots, freeCols)
        }
    }
}

private const val EPS = 1e-9

private fun ref(mat: Array<DoubleArray>): IntArray {
    val m = mat.size
    val n = mat[0].size - 1 // augmented, so last column is rhs
    val pivots = IntArray(n) { -1 }
    var row = 0

    for (col in 0 until n) {
        // find best pivot for this column
        // by searching from row..(m-1) for the largest absolute value in mat[r][col]
        var bestRow = -1
        var bestVal = 0.0
        for (r in row until m) {
            val v = abs(mat[r][col])
            if (v > bestVal + EPS) {
                bestVal = v
                bestRow = r
            }
        }
        // no suitable pivot found (all 0), so skip this column
        if (bestRow == -1 || bestVal < EPS) continue

        // swap best row into position
        val tmp = mat[row]
        mat[row] = mat[bestRow]
        mat[bestRow] = tmp

        // normalize pivot row, so pivot element is 1
        val pivot = mat[row][col]
        for (c in col until n + 1) {
            mat[row][c] /= pivot
        }

        // eliminate this column from all other rows
        for (r in 0 until m) {
            if (r == row) continue
            val factor = mat[r][col]
            if (abs(factor) < EPS) continue // already basically zero
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
private fun reconstructSolution(system: LinearSystem, freeValues: DoubleArray): DoubleArray {
    val (mat, pivots, freeCols) = system
    val n = pivots.size
    val x = DoubleArray(n)

    // fill in free variables in solution
    for (i in 0 until freeCols.size) {
        x[freeCols[i]] = freeValues[i]
    }

    val lastCol = mat[0].size - 1

    // for each variable with a pivot, compute its value from the rest of the row
    // the row has the form: x_pivot + sum(coeff_i * x_free_i) = rhs
    // so x_pivot = rhs - sum(coeff_i * x_free_i)
    for (col in 0 until n) {
        val r = pivots[col] // row for this pivot column
        if (r == -1) continue // if free variable, skip

        var value = mat[r][lastCol] // rhs

        // subtract (coeff * x_free) for each free variable in this row
        for (i in 0 until freeCols.size) {
            val fcol = freeCols[i]
            val coeff = mat[r][fcol]
            if (abs(coeff) > EPS) {
                value -= coeff * x[fcol]
            }
        }
        x[col] = value
    }

    return x
}

private fun validateAndSum(x: DoubleArray, maxPress: IntArray): Int? {
    var sumPress = 0
    val n = x.size
    for (col in 0 until n) {
        val v = x[col]
        if (v < -EPS) return null // negative
        val rounded = round(v).toInt()
        if (abs(v - rounded) > EPS) return null // not integer
        if (rounded > maxPress[col]) return null // exceeds max
        sumPress += rounded
    }
    return sumPress
}

private fun minimize(system: LinearSystem, maxPress: IntArray): Int {
    var best: Int? = null

    fun dfs(idx: Int, freeValues: DoubleArray) {
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
            freeValues[idx] = press.toDouble()
            dfs(idx + 1, freeValues)
        }
    }

    dfs(0, DoubleArray(system.freeCols.size))

    return best ?: error("no valid solution for line, uh oh!")
}

fun main() = PuzzleInput(2025, 10).withSolutions({ part1() }, { part2() }).run()