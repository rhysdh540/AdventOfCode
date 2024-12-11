import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_19(input: String): Any? {
    val (mappings0, molecule) = input.split("\n\n")
    // mappings can't be a map because there can be multiple mappings for the same molecule
    val mappings = mappings0.lines().map { it.split(" => ").let { it[0] to it[1] } }
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

fun part2_19(input: String): Any? {
    return part2_19_bf(input)
    // All of the smart stuff from this solution is thanks to this comment from r/adventofcode:
    // https://www.reddit.com/r/adventofcode/comments/3xflz8/day_19_solutions/cy4etju/
    val (_, molecule) = input.split("\n\n")

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

// regular brute-force solution
fun part2_19_bf(input: String): Any? {
    // reverse the molecule so we can match the longest possible molecule first
    val (mappings0, moleculeReversed) = input.split("\n\n").let {
        it[0] to it[1].reversed()
    }

    val mappings = mappings0.split("\n").associate {
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

fun main() {
    val input = Path("inputs/2015/19.txt").readText()

    var start = System.nanoTime()
    var result = part1_19(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_19(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}