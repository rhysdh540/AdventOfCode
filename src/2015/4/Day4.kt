import java.security.MessageDigest
import java.util.stream.IntStream
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_4(input: String): Any? {
    return Day4.run(input, "00000")
}

fun part2_4(input: String): Any? {
    return Day4.run(input, "000000", parallel = true)
}

object Day4 {
    fun run(input: String, match: String, parallel: Boolean = false): Int {
        val md5 = ThreadLocal.withInitial { MessageDigest.getInstance("MD5") }

        val stream = IntStream.iterate(0) { i -> i + 1 }.filter { i ->
            val hash = md5.get().digest((input + i).toByteArray())
            hash.hexMatches(match)
        }

        if (parallel) {
            stream.parallel()
        }

        return stream.findFirst().asInt
    }

    fun ByteArray.hexMatches(match: String): Boolean {
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
}

fun main() {
    val input = Path("inputs/2015/4.txt").readText()

    var start = System.nanoTime()
    var result = part1_4(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_4(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}