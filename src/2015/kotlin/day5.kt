import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return lines.count {
        val vowels = it.count { c -> c in "aeiou" }
        val double = it.zipWithNext().any { (a, b) -> a == b }
        val forbidden = "ab" in it || "cd" in it || "pq" in it || "xy" in it
        vowels >= 3 && double && !forbidden
    }
}

private fun PuzzleInput.part2(): Any? {
    val rPair = Regex(""".*(..).*\1.*""")
    val rRepeat = Regex(""".*(.).\1.*""")
    return lines.count {
        it.matches(rPair) && it.matches(rRepeat)
    }
}

fun main() = PuzzleInput(2015, 5).withSolutions({ part1() }, { part2() }).run()