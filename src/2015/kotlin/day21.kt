import dev.rdh.aoc.*

private fun PuzzleInput.part1(): Any? {
    val (bossHP, bossDamage, bossArmor) = lines.map { it.split(": ")[1].toInt() }

    val items = getItems()
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

private fun PuzzleInput.part2(): Any? {
    val (bossHP, bossDamage, bossArmor) = input.lines().map { it.split(": ")[1].toInt() }

    val items = getItems()
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

private data class Item(val cost: Int, val damage: Int, val armor: Int)
private data class Kit(val weapon: Item, val armor: Item, val ring1: Item, val ring2: Item)

private fun getItems(): List<Kit> {
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
    """.trimIndent().split(blankLines).map { chunk ->
        chunk.lines().drop(1).map { line ->
            val (c, d, a) = line.split(Regex("\\s+")).filter { it.toIntOrNull() != null && '+' !in it }.ints
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

    return allItems
}

private fun simulateFight(playerDamage: Int, playerArmor: Int, bossHP: Int, bossDamage: Int, bossArmor: Int): Boolean {
    var player = 100
    var boss = bossHP
    while (player > 0 && boss > 0) {
        boss -= maxOf(playerDamage - bossArmor, 1)
        if (boss <= 0) return true
        player -= maxOf(bossDamage - playerArmor, 1)
    }
    return false
}

fun main() = PuzzleInput(2015, 21).withSolutions({ part1() }, { part2() }).run()