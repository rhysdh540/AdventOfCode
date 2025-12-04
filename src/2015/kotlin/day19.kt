import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (mappings0, molecule) = splitBy(blankLines)
    // mappings can't be a map because there can be multiple mappings for the same molecule
    val mappings = mappings0.lines().map { it.split(" => ").toPair() }
    val molecules = mutableSetOf<String>()
    for ((from, to) in mappings) {
        var i = 0
        while (i < molecule.length) {
            val j = molecule.indexOf(from, i)
            if (j == -1) break
            molecules.add(molecule.substring(0, j) + to + molecule.substring(j + from.length))
            i = j + 1
        }
    }

    return molecules.size
}

// All of the smart stuff from this solution is thanks to this comment from r/adventofcode:
// https://www.reddit.com/r/adventofcode/comments/3xflz8/day_19_solutions/cy4etju/
private fun PuzzleInput.part2(): Any? {
    val (_, molecule) = splitBy(blankLines)

    // essentially, there are 3 kinds of mappings
    // X -> XX, where X is any element
    // X -> X(X), where () are Rn and Ar
    // X -> X(X,X), where , is Y
    // through some funny math that I only partially get, we can calculate the number of steps
    // "My request for the "fewest number of steps" is a decoy - there is only one number of steps possible" - AOC guy

    // Count the number of tokens (elements, including Rn, Ar, and Y)
    val tokens = Regex("[A-Z][a-z]?").findAll(molecule).count()

    // Count the number of '(' and ')'
    val rnArCount = molecule.windowed(2).count { it == "Rn" || it == "Ar" }

    // Count the number of ','
    val yCount = molecule.count { it == 'Y' }

    // Apply the formula to calculate steps
    // each X => XX (un)transformation takes 1 token (the extra element)
    // each X => X(X) (aka X => XRnXAr) transformation takes 3 tokens (similar to the last, but with the Rn and Ar)
    // each X => X(X,X) (aka X => XRnXYXAr) is similar to the X(X), but each `,` reduces the length by 2 (since you're removing the , and the X after it)
    //  we don't include the first X or Rn/Ar since removing all the `,X` will leave us with a (X) (which will then get taken care of by the second rule)
    // so the formula is tokens - count(Rn or Ar) - 2 * count(,) - 1 because we start with 1 token (the `e`)
    return tokens - rnArCount - 2 * yCount - 1
}

// this works too
private fun PuzzleInput.part2bruteForce(): Any? {
    val (mappings0, moleculeReversed) = splitBy(blankLines).let {
        it[0] to it[1].reversed()
    }

    val mappings = mappings0.lines().associate {
        it.split(" => ").let { (a, b) -> b.reversed() to a.reversed() }
    }

    val regex = Regex(mappings.keys.joinToString("|"))
    return generateSequence(moleculeReversed) {
        if (it == "e") null
        else regex.find(it)!!.let { r ->
            it.replaceRange(r.range, mappings[r.value]!!)
        }
    }.count() - 1
}

fun main() = PuzzleInput(2015, 19).withSolutions({ part1() }, { part2() }).run()