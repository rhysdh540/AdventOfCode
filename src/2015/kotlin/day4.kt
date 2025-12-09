import dev.rdh.aoc.*
import java.security.MessageDigest
import java.util.stream.IntStream
import kotlin.experimental.and

private fun PuzzleInput.part1(): Any? {
    return solve {
        it[0] == 0.toByte() && it[1] == 0.toByte() && (it[2] and 0xF0.toByte()) == 0.toByte()
    }
}

private fun PuzzleInput.part2(): Any? {
    return solve(parallel = true) {
        it[0] == 0.toByte() && it[1] == 0.toByte() && it[2] == 0.toByte()
    }
}

private inline fun PuzzleInput.solve(parallel: Boolean = false, crossinline condition: (ByteArray) -> Boolean): Int {
    val inputBytes = input.toByteArray()
    val md5 = ThreadLocal.withInitial { MessageDigest.getInstance("MD5") }
    return IntStream.iterate(0, Int::inc).let { if (parallel) it.parallel() else it }.filter { n ->
        md5.get().apply {
            reset()
            update(inputBytes)
            update(n.toString().toByteArray())
        }.let { condition(it.digest()) }
    }.findFirst().asInt
}

fun main() = PuzzleInput(2015, 4).withSolutions({ part1() }, { part2() }).run()