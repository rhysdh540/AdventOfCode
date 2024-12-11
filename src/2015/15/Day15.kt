import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_15(input: String): Any? {
    return Day15.run(input)
}

fun part2_15(input: String): Any? {
    return Day15.run(input, 500)
}

object Day15 {
    data class Ingredient(val name: String, val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int)

    fun run(input: String, calorieCount: Int? = null): Int {
        val ingredients = input.lines().map {
            val (name, capacity, durability, flavor, texture, calories) = Regex("""(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)""").find(it)!!.destructured
            Ingredient(name, capacity.toInt(), durability.toInt(), flavor.toInt(), texture.toInt(), calories.toInt())
        }

        val combinations = (0..100).flatMap { a ->
            (0..100 - a).flatMap { b ->
                (0..100 - a - b).map { c ->
                    listOf(a, b, c, 100 - a - b - c)
                }
            }
        }

        return combinations.map { l ->
            val capacity = ingredients.zip(l).sumOf { (i, n) -> i.capacity * n }
            val durability = ingredients.zip(l).sumOf { (i, n) -> i.durability * n }
            val flavor = ingredients.zip(l).sumOf { (i, n) -> i.flavor * n }
            val texture = ingredients.zip(l).sumOf { (i, n) -> i.texture * n }
            val calories = ingredients.zip(l).sumOf { (i, n) -> i.calories * n }

            if (capacity < 0 || durability < 0 || flavor < 0 || texture < 0 || (calorieCount != null && calories != calorieCount)) {
                return@map 0
            }

            return@map capacity * durability * flavor * texture
        }.max()
    }
}

fun main() {
    val input = Path("inputs/2015/15.txt").readText()

    var start = System.nanoTime()
    var result = part1_15(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_15(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}