import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_4(input: String): Any? {
    val rooms = input.lines().map {
        val (name, id, checksum) = Regex("""([a-z-]+)-(\d+)\[([a-z]+)]""").find(it)!!.destructured
        Triple(name.replace("-", ""), id.toInt(), checksum)
    }

    return rooms.filter { room ->
        room.third == room.first.groupBy { it }
            .mapValues { it.value.size }
            .toList()
            .sortedWith(compareBy({ -it.second }, { it.first }))
            .take(5)
            .map { it.first }
            .joinToString("")
    }.sumOf { it.second }
}

fun part2_4(input: String): Any? {
    val rooms = input.lines().map {
        val (name, id, checksum) = Regex("""([a-z-]+)-(\d+)\[([a-z]+)]""").find(it)!!.destructured
        Triple(name.replace("-", " "), id.toInt(), checksum)
    }

    return rooms.map { room ->
        val decrypted = room.first.map {
            if (it == ' ') ' '
            else ((it - 'a' + room.second) % 26 + 'a'.code).toChar()
        }.joinToString("")
        Pair(decrypted, room.second)
    }.find { it.first == "northpole object storage" }?.second
}

fun main() {
    val input = Path("inputs/2016/4.txt").readText()

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