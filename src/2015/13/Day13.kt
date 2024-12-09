import Day13.permutations
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_13(input: String): Any? {
    val happinesses = mutableMapOf<String, MutableMap<String, Int>>()

    input.lines().forEach {
        val (person, gainOrLose, amount, neighbor) = Regex("""(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+).""").find(it)!!.destructured
        val value = if(gainOrLose == "gain") amount.toInt() else -amount.toInt()
        happinesses.getOrPut(person) { mutableMapOf() }[neighbor] = value
    }

    val people = happinesses.keys.toList()
    val permutations = people.permutations()

    return permutations.maxOfOrNull { perm ->
        perm.windowed(2).sumOf { (a, b) ->
            happinesses[a]!![b]!! + happinesses[b]!![a]!!
        } + happinesses[perm.first()]!![perm.last()]!! + happinesses[perm.last()]!![perm.first()]!!
    }
}

fun part2_13(input: String): Any? {
    val happinesses = mutableMapOf<String, MutableMap<String, Int>>()

    input.lines().forEach {
        val (person, gainOrLose, amount, neighbor) = Regex("""(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+).""").find(it)!!.destructured
        val value = if(gainOrLose == "gain") amount.toInt() else -amount.toInt()
        happinesses.getOrPut(person) { mutableMapOf() }[neighbor] = value
    }

    happinesses["Me"] = mutableMapOf()
    happinesses.keys.forEach {
        if(it != "Me") {
            happinesses[it]!!["Me"] = 0
            happinesses["Me"]!![it] = 0
        }
    }

    val people = happinesses.keys.toList()
    val permutations = people.permutations()

    return permutations.maxOfOrNull { perm ->
        perm.windowed(2).sumOf { (a, b) ->
            happinesses[a]!![b]!! + happinesses[b]!![a]!!
        } + happinesses[perm.first()]!![perm.last()]!! + happinesses[perm.last()]!![perm.first()]!!
    }
}

object Day13 {
    fun <T> List<T>.permutations(): List<List<T>> {
        if (size == 1) return listOf(this)
        val perms = mutableListOf<List<T>>()
        val toInsert = first()
        for (perm in drop(1).permutations()) {
            for (i in 0..perm.size) {
                val newPerm = perm.toMutableList()
                newPerm.add(i, toInsert)
                perms.add(newPerm)
            }
        }
        return perms
    }
}

fun main() {
    val input = Path("inputs/2015/13.txt").readText()

    var start = System.nanoTime()
    var result = part1_13(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_13(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}