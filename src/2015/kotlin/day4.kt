import dev.rdh.aoc.*
import java.security.MessageDigest
import java.util.stream.IntStream

private fun PuzzleInput.part1(): Any? {
    return run("00000")
}

private fun PuzzleInput.part2(): Any? {
    return run("000000", parallel = true)
}

private fun PuzzleInput.run(match: String, parallel: Boolean = false): Int {
    val md5 = ThreadLocal.withInitial { MessageDigest.getInstance("MD5") }

    val stream = IntStream.iterate(0) { it + 1 }.filter {
        md5.get().digest((input + it).toByteArray()).hexMatches(match)
    }

    if (parallel) {
        stream.parallel()
    }

    return stream.findFirst().asInt
}

private fun ByteArray.hexMatches(match: String): Boolean {
    val hexChars = "0123456789abcdef".toCharArray()
    val len = match.length

    for (i in 0 until len) {
        val byteIndex = i / 2
        if (byteIndex >= this.size) {
            return false
        }
        val byte = this[byteIndex].toInt() and 0xFF

        val expectedChar = if (i and 1 == 0) {
            hexChars[byte ushr 4]
        } else {
            hexChars[byte and 0x0F]
        }

        if (expectedChar != match[i]) {
            return false
        }
    }
    return true
}

fun main() = PuzzleInput(2015, 4).withSolutions({ part1() }, { part2() }).run()