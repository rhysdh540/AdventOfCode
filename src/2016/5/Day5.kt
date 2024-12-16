import Day5.hexNext
import java.security.MessageDigest
import java.util.stream.IntStream
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_5(input: String): Any? {
    val password = CharArray(8)
    var index = 0

    val md5 = ThreadLocal.withInitial { MessageDigest.getInstance("MD5") }

    val stream = IntStream.iterate(0) { i -> i + 1 }.mapToObj { i ->
        val hash = md5.get().digest((input + i).toByteArray())
        hash.hexNext("00000")
    }.filter { it != null }.map { it!! }.parallel()

    stream.limit(8).forEachOrdered {
        password[index++] = it.first
    }

    return String(password)
}

fun part2_5(input: String): Any? {
    val password = CharArray(8)
    val md5 = ThreadLocal.withInitial { MessageDigest.getInstance("MD5") }

    val stream = IntStream.iterate(0) { i -> i + 1 }.mapToObj { i ->
        val hash = md5.get().digest((input + i).toByteArray())
        hash.hexNext("00000")
    }.filter { it != null }.map { it!! }.parallel()

    stream.iterator().forEach { // use iterator so we can return early
        val (index, char) = it
        if (index in '0'..'7' && password[index - '0'] == 0.toChar()) {
            password[index - '0'] = char
        }

        if (password.all { it != 0.toChar() }) {
            return String(password)
        }
    }

    error("Unreachable")
}

object Day5 {
    fun ByteArray.hexNext(match: String): Pair<Char, Char>? {
        val hexChars = "0123456789abcdef".toCharArray()
        val len = match.length

        fun getChar(i: Int): Char? {
            val byteIndex = i / 2
            if (byteIndex >= this.size) {
                return null
            }
            val byte = this[byteIndex].toInt() and 0xFF

            return if (i and 1 == 0) {
                hexChars[byte ushr 4]
            } else {
                hexChars[byte and 0x0F]
            }
        }

        for (i in 0 until len) {
            val expectedChar = getChar(i) ?: return null
            if (expectedChar != match[i]) {
                return null
            }
        }

        return getChar(len)!! to getChar(len + 1)!!
    }
}

fun main() {
    val input = Path("inputs/2016/5.txt").readText()

    var start = System.nanoTime()
    var result = part1_5(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_5(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}