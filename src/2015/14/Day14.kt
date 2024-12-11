import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_14(input: String): Any? {
    val reindeers = input.lines().map {
        val (n, s, f, r) = Regex("""(.+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""").find(it)!!.destructured
        Day14.Reindeer(n, s.toInt(), f.toInt(), r.toInt())
    }

    return return reindeers.maxOf { it.distance(2503) }
}

fun part2_14(input: String): Any? {
    val reindeers = input.lines().map {
        val (n, s, f, r) = Regex("""(.+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""").find(it)!!.destructured
        Day14.Reindeer(n, s.toInt(), f.toInt(), r.toInt())
    }

    val scores = mutableMapOf<String, Int>().withDefault { 0 }

    for (i in 1..2503) {
        val distances = reindeers.map { it.distance(i) }
        val max = distances.max()
        reindeers.filterIndexed { index, _ -> distances[index] == max }.forEach { scores[it.name] = scores.getValue(it.name) + 1 }
    }

    return scores.values.max()
}

object Day14 {
    data class Reindeer(val name: String, val speed: Int, val flyTime: Int, val restTime: Int) {
        fun distance(time: Int): Int {
            val cycleTime = flyTime + restTime
            val cycles = time / cycleTime
            val remaining = time % cycleTime
            val fly = if (remaining > flyTime) flyTime else remaining
            return cycles * flyTime * speed + fly * speed
        }
    }
}

fun main() {
    val input = Path("inputs/2015/14.txt").readText()

    var start = System.nanoTime()
    var result = part1_14(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_14(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}