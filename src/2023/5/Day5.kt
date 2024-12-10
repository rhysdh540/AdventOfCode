import Day5.Map.Entry
import Day5.Map
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_5(input: String): Any? {
    val parsedInput = input.split("\n\n").map { it.split("\n") }
    val seeds = parsedInput[0][0].substring(7).split(" ").map { it.toLong() }
    val maps = parsedInput.drop(1).map {
        Map(it.drop(1).map(Entry::of).sortedBy(Entry::destination).toTypedArray())
    }

    return seeds.minOf { maps.fold(it) { acc, map -> map.getValue(acc) } }
}

fun part2_5(input: String): Any? {
    val parsedInput = input.split("\n\n").map { it.split("\n") }
    val seeds = parsedInput[0][0].substring(7).split(" ").map { it.toLong() }
    val maps = parsedInput.drop(1).map {
        Map(it.drop(1).map(Entry::of).sortedBy(Entry::destination).toTypedArray())
    }

    val ranges = seeds.chunked(2).map { it[0] until it[0] + it[1] }

    for (next in maps.last().maxRange()) {
        val changed = maps.foldRight(next) { map, acc -> map.getKey(acc) }
        if (ranges.any { changed in it }) {
            return next
        }
    }

    error("No solution found")
}

object Day5 {
    data class Map(val entries: Array<Entry>) {
        data class Entry(val destination: Long, val source: Long, val range: Long) {
            companion object {
                fun of(line: String): Entry {
                    val (d, s, r) = line.split(" ").map { it.toLong() }
                    return Entry(d, s, r)
                }
            }
        }

        fun getValue(key: Long): Long {
            entries.forEach {
                if(key in it.source until it.source + it.range) {
                    return it.destination + (key - it.source)
                }
            }

            return key
        }

        fun getKey(key: Long): Long {
            var low = 0
            var high = entries.size - 1
            while (low <= high) {
                val mid = (low + high) ushr 1
                val midVal = entries[mid]
                when {
                    midVal.destination < key -> low = mid + 1
                    midVal.destination > key -> high = mid - 1
                    else -> return midVal.source
                }
            }

            return key
        }

        fun maxRange() = 0..entries.maxOf { it.range }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Map

            return entries.contentEquals(other.entries)
        }

        override fun hashCode(): Int {
            return entries.contentHashCode()
        }
    }
}

fun main() {
    val input = Path("inputs/2023/5.txt").readText()

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