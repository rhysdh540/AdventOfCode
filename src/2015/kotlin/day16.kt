import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val properties = getProperties()
    return run { sue ->
        sue.properties.all { (key, value) ->
            properties[key] == value
        }
    }
}

private fun PuzzleInput.part2(): Any? {
    val properties = getProperties()
    return run { sue ->
        sue.properties.all { (key, value) ->
            when (key) {
                "cats", "trees" -> properties[key]!! < value
                "pomeranians", "goldfish" -> properties[key]!! > value
                else -> properties[key] == value
            }
        }
    }
}

private inline fun PuzzleInput.run(selector: (Sue) -> Boolean): Any? {
    val aunts = lines.map {
        val (num, things) = Regex("Sue (\\d+): (.+)").find(it)!!.destructured
        val properties = things.split(", ").associate {
            val (key, value) = it.split(": ")
            key to value.toInt()
        }
        Sue(num.toInt(), properties)
    }

    return aunts.first(selector).number
}

private data class Sue(val number: Int, val properties: Map<String, Int>)

fun getProperties() = """
        children: 3
        cats: 7
        samoyeds: 2
        pomeranians: 3
        akitas: 0
        vizslas: 0
        goldfish: 5
        trees: 3
        cars: 2
        perfumes: 1
    """.trimIndent().lines().associate {
    val (key, value) = it.split(": ")
    key to value.toInt()
}

fun main() = PuzzleInput(2015, 16).withSolutions({ part1() }, { part2() }).run()