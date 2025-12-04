import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return reindeers.maxOf { it.distance(2503) }
}

private fun PuzzleInput.part2(): Any? {
    val reindeers = reindeers
    val scores = mutableMapOf<String, Int>().withDefault { 0 }

    for (i in 1..2503) {
        val distances = reindeers.map { it.distance(i) }
        val max = distances.max()
        reindeers.filterIndexed { index, _ -> distances[index] == max }
            .forEach { scores[it.name] = scores.getValue(it.name) + 1 }
    }

    return scores.values.max()
}

private val PuzzleInput.reindeers: List<Reindeer>
    get() {
        val regex = Regex("""(.+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""")
        return lines.map {
            val (n, s, f, r) = regex.find(it)!!.destructured
            Reindeer(n, s.toInt(), f.toInt(), r.toInt())
        }
    }

private data class Reindeer(val name: String, val speed: Int, val flyTime: Int, val restTime: Int) {
    fun distance(time: Int): Int {
        val cycleTime = flyTime + restTime
        val cycles = time / cycleTime
        val remaining = time % cycleTime
        val fly = if (remaining > flyTime) flyTime else remaining
        return cycles * flyTime * speed + fly * speed
    }
}

fun main() = PuzzleInput(2015, 14).withSolutions({ part1() }, { part2() }).run()