import Day21.simulateFight
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_21(input: String): Any? {
    val (bossHP, bossDamage, bossArmor) = input.lines().map { it.split(": ")[1].toInt() }

    val items = Day21.items
    return items.minOf { kit ->
        val playerDamage = kit.weapon.damage + kit.ring1.damage + kit.ring2.damage
        val playerArmor = kit.armor.armor + kit.ring1.armor + kit.ring2.armor
        if (simulateFight(playerDamage, playerArmor, bossHP, bossDamage, bossArmor)) {
            kit.weapon.cost + kit.armor.cost + kit.ring1.cost + kit.ring2.cost
        } else {
            Int.MAX_VALUE
        }
    }
}

fun part2_21(input: String): Any? {
    val (bossHP, bossDamage, bossArmor) = input.lines().map { it.split(": ")[1].toInt() }

    val items = Day21.items
    return items.maxOf { kit ->
        val playerDamage = kit.weapon.damage + kit.ring1.damage + kit.ring2.damage
        val playerArmor = kit.armor.armor + kit.ring1.armor + kit.ring2.armor
        if (!simulateFight(playerDamage, playerArmor, bossHP, bossDamage, bossArmor)) {
            kit.weapon.cost + kit.armor.cost + kit.ring1.cost + kit.ring2.cost
        } else {
            Int.MIN_VALUE
        }
    }
}

object Day21 {
    data class Item(val cost: Int, val damage: Int, val armor: Int)
    data class Kit(val weapon: Item, val armor: Item, val ring1: Item, val ring2: Item)

    val items: List<Kit> = run {
        val (weapons, armors, rings) = """
        Weapons:    Cost  Damage  Armor
        Dagger        8     4       0
        Shortsword   10     5       0
        Warhammer    25     6       0
        Longsword    40     7       0
        Greataxe     74     8       0

        Armor:      Cost  Damage  Armor
        Leather      13     0       1
        Chainmail    31     0       2
        Splintmail   53     0       3
        Bandedmail   75     0       4
        Platemail   102     0       5

        Rings:      Cost  Damage  Armor
        Damage +1    25     1       0
        Damage +2    50     2       0
        Damage +3   100     3       0
        Defense +1   20     0       1
        Defense +2   40     0       2
        Defense +3   80     0       3
    """.trimIndent().split("\n\n").map {
            it.lines().drop(1).map {
                val (c, d, a) = it.split(Regex("\\s+")).filter { it.toIntOrNull() != null && '+' !in it }.map { it.toInt() }
                Item(c, d, a)
            }
        }

        val allItems = mutableListOf<Kit>()
        for (weapon in weapons) {
            for (armor in armors + Item(0, 0, 0)) {
                for (ring1 in rings + Item(0, 0, 0)) {
                    for (ring2 in rings + Item(0, 0, 0)) {
                        if (ring1 == ring2 && ring1 != Item(0, 0, 0)) continue
                        allItems.add(Kit(weapon, armor, ring1, ring2))
                    }
                }
            }
        }

//        return allItems.minOf { kit ->
//            val playerDamage = kit.weapon.damage + kit.ring1.damage + kit.ring2.damage
//            val playerArmor = kit.armor.armor + kit.ring1.armor + kit.ring2.armor
//            if (simulateFight(playerDamage, playerArmor)) {
//                kit.weapon.cost + kit.armor.cost + kit.ring1.cost + kit.ring2.cost
//            } else {
//                Int.MAX_VALUE
//            }
//        }

        allItems
    }

    fun simulateFight(playerDamage: Int, playerArmor: Int, bossHP: Int, bossDamage: Int, bossArmor: Int): Boolean {
        var player = 100
        var boss = bossHP
        while (player > 0 && boss > 0) {
            boss -= maxOf(playerDamage - bossArmor, 1)
            if (boss <= 0) return true
            player -= maxOf(bossDamage - playerArmor, 1)
        }
        return false
    }
}

fun main() {
    val input = Path("inputs/2015/21.txt").readText()

    var start = System.nanoTime()
    var result = part1_21(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_21(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}