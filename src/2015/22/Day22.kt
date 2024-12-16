import Day22.Player
import kotlin.io.path.Path
import kotlin.io.path.readText

fun part1_22(input: String): Any? {
    val boss = input.lines()
        .map { it.split(": ")[1].toInt() }
        .let { Player(hp = it[0], damage = it[1]) }

    return Day22.play(boss)
}

fun part2_22(input: String): Any? {
    val boss = input.lines()
        .map { it.split(": ")[1].toInt() }
        .let { Player(hp = it[0], damage = it[1]) }

    return Day22.play(boss, hardMode = true)
}

object Day22 {
    data class Spell(
        val name: String,
        val cost: Int,
        val damage: Int = 0,
        val heal: Int = 0,
        val armor: Int = 0,
        val mana: Int = 0,
        val duration: Int = 0,
        val effect: Effect = Effect()
    )

    val magicMissile = Spell("Magic Missile", 53, damage = 4)
    val drain = Spell("Drain", 73, damage = 2, heal = 2)
    val shield = Spell(
        "Shield", 113, duration = 6, effect = Effect(
        onStart = { player, _ -> player.armor += 7 },
        onEnd = { player, _ -> player.armor -= 7 }
    ))
    val poison = Spell(
        "Poison", 173, duration = 6, effect = Effect(
        tick = { _, boss -> boss.hp -= 3 }
    ))
    val recharge = Spell(
        "Recharge", 229, duration = 5, effect = Effect(
        tick = { player, _ -> player.mana += 101 }
    ))

    val spells = listOf(magicMissile, drain, shield, poison, recharge)

    data class Effect(
        val onStart: (player: Player, boss: Player) -> Unit = { _, _ -> },
        val onEnd: (player: Player, boss: Player) -> Unit = { _, _ -> },
        val tick: (player: Player, boss: Player) -> Unit = { _, _ -> }
    )

    data class Player(
        var hp: Int,
        var damage: Int = 0,
        var mana: Int = 0,
        var armor: Int = 0,
        var effects: MutableMap<Spell, Int> = mutableMapOf()
    )

    fun play(
        boss: Player,
        player: Player = Player(hp = 50, mana = 500),
        manaSpent: Int = 0,
        hardMode: Boolean = false,
        playerTurn: Boolean = true,
        minMana: Int = Int.MAX_VALUE
    ): Int {
        var minMana = minMana
        if (hardMode && playerTurn) {
            player.hp--
            if (player.hp <= 0) return minMana
        }

        val expired = mutableListOf<Spell>() // don't cause CMEs
        player.effects.forEach { (spell, duration) ->
            spell.effect.tick(player, boss)
            if (duration == 1) {
                spell.effect.onEnd(player, boss)
                expired.add(spell)
            } else {
                player.effects[spell] = duration - 1
            }
        }
        expired.forEach { player.effects.remove(it) }

        if (boss.hp <= 0) {
            minMana = minMana.coerceAtMost(manaSpent)
            return minMana
        }

        if (playerTurn) {
            if (player.mana < spells.minOf { it.cost } || manaSpent >= minMana) return minMana

            for (spell in spells) {
                if (spell.cost <= player.mana && spell !in player.effects.keys) {
                    val newPlayer = player.copy(
                        hp = player.hp,
                        damage = player.damage,
                        mana = player.mana - spell.cost,
                        armor = player.armor,
                        effects = player.effects.toMutableMap()
                    )
                    val newBoss = boss.copy()

                    val newManaSpent = manaSpent + spell.cost
                    if (newManaSpent >= minMana) continue

                    if (spell.duration > 0) {
                        // duration spell
                        newPlayer.effects[spell] = spell.duration
                        spell.effect.onStart(newPlayer, newBoss)
                    } else {
                        // instant spell
                        if (spell.damage > 0) newBoss.hp -= spell.damage
                        if (spell.heal > 0) newPlayer.hp += spell.heal
                    }

                    if (newBoss.hp <= 0) {
                        minMana = minMana.coerceAtMost(newManaSpent)
                        continue
                    }

                    minMana = minMana.coerceAtMost(
                        play(
                            newPlayer,
                            newBoss,
                            newManaSpent,
                            hardMode,
                            playerTurn = false,
                            minMana = minMana
                        )
                    )
                }
            }
        } else {
            // boss's turn
            val damageDealt = (boss.damage - player.armor).coerceAtLeast(1)
            player.hp -= damageDealt

            if (player.hp > 0) {
                minMana = minMana.coerceAtMost(
                    play(
                        player,
                        boss,
                        manaSpent,
                        hardMode,
                        playerTurn = true,
                        minMana = minMana
                    )
                )
            }
        }

        return minMana
    }
}

fun main() {
    val input = Path("inputs/2015/22.txt").readText()

    var start = System.nanoTime()
    var result = part1_22(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_22(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}