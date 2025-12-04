import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    return run()
}

private fun PuzzleInput.part2(): Any? {
    return run(calorieCount = 500)
}

private data class Ingredient(
    val name: String,
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int
)

private fun PuzzleInput.run(calorieCount: Int? = null): Int {
    val ingredients = lines.map {
        val (name, capacity, durability, flavor, texture, calories) = Regex("""(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)""").find(
            it
        )!!.destructured
        Ingredient(name, capacity.toInt(), durability.toInt(), flavor.toInt(), texture.toInt(), calories.toInt())
    }

    val combinations = (0..100).flatMap { a ->
        (0..100 - a).flatMap { b ->
            (0..100 - a - b).map { c ->
                listOf(a, b, c, 100 - a - b - c)
            }
        }
    }

    return combinations.maxOf { l ->
        val capacity = ingredients.zip(l).sumOf { (i, n) -> i.capacity * n }
        val durability = ingredients.zip(l).sumOf { (i, n) -> i.durability * n }
        val flavor = ingredients.zip(l).sumOf { (i, n) -> i.flavor * n }
        val texture = ingredients.zip(l).sumOf { (i, n) -> i.texture * n }
        val calories = ingredients.zip(l).sumOf { (i, n) -> i.calories * n }

        if (capacity < 0 || durability < 0 || flavor < 0 || texture < 0 || (calorieCount != null && calories != calorieCount)) {
            return@maxOf 0
        }

        return@maxOf capacity * durability * flavor * texture
    }
}

fun main() = PuzzleInput(2015, 15).withSolutions({ part1() }, { part2() }).run()