import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_16(input: String): Any? {
    val aunts = input.lines().map {
        val (num, things) = Regex("Sue (\\d+): (.+)").find(it)!!.destructured
        val properties = things.split(", ").associate {
            val (key, value) = it.split(": ")
            key to value.toInt()
        }
        Day16.Sue(num.toInt(), properties)
    }

    val properties = """
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

    return aunts.first { aunt ->
        aunt.properties.all { (key, value) ->
            properties[key] == value
        }
    }.number
}

fun part2_16(input: String): Any? {
    val aunts = input.lines().map {
        val (num, things) = Regex("Sue (\\d+): (.+)").find(it)!!.destructured
        val properties = things.split(", ").associate {
            val (key, value) = it.split(": ")
            key to value.toInt()
        }
        Day16.Sue(num.toInt(), properties)
    }

    val properties = """
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

    return aunts.first { aunt ->
        aunt.properties.all { (key, value) ->
            when (key) {
                "cats", "trees" -> properties[key]!! < value
                "pomeranians", "goldfish" -> properties[key]!! > value
                else -> properties[key] == value
            }
        }
    }.number
}

object Day16 {
    data class Sue(val number: Int, val properties: Map<String, Int>)
}

fun main() {
    val input = Path("inputs/2015/16.txt").readText()

    var start = System.nanoTime()
    var result = part1_16(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_16(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}