import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val password = input.toCharArray()
    while (true) {
        password.increment()
        if (isValid(password)) {
            return String(password)
        }
    }
}

private fun PuzzleInput.part2(): Any? {
    return PuzzleInput(part1() as String).part1()
}

private fun CharArray.increment() {
    for (i in size - 1 downTo 0) {
        if (this[i] == 'z') {
            this[i] = 'a'
        } else {
            this[i] = this[i] + 1
            break
        }
    }
}

private fun isValid(password: CharArray): Boolean {
    val hasStraight = (0 until password.size - 2).any {
        password[it] + 1 == password[it + 1] && password[it + 1] + 1 == password[it + 2]
    }
    if (!hasStraight) return false

    val hasNoIOL = password.none { it == 'i' || it == 'o' || it == 'l' }
    if (!hasNoIOL) return false

    val hasTwoPairs = String(password).windowed(2).filter { it[0] == it[1] }.distinct().size >= 2
    return hasTwoPairs
}

fun main() = PuzzleInput(2015, 11).withSolutions({ part1() }, { part2() }).run()