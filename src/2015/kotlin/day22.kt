import dev.rdh.aoc.*
import kotlin.math.min

private fun PuzzleInput.part1(): Any? {
    val boss = input.lines()
        .map { it.split(": ")[1].toInt() }
        .let { Player(hp = it[0], damage = it[1]) }

    return play(boss)
}

private fun PuzzleInput.part2(): Any? {
    val boss = input.lines()
        .map { it.split(": ")[1].toInt() }
        .let { Player(hp = it[0], damage = it[1]) }

    return play(boss, hardMode = true)
}

private enum class Spell(
    val cost: Int,
    val damage: Int = 0,
    val heal: Int = 0,
    val duration: Int = 0,
    val effect: Effect = Effect()
) {
    MagicMissile(53, damage = 4),
    Drain(73, damage = 2, heal = 2),
    Shield(113, duration = 6, effect = Effect(
        onStart = { player, _ -> player.armor += 7 },
        onEnd = { player, _ -> player.armor -= 7 }
    )),
    Poison(173, duration = 6, effect = Effect(
        tick = { _, boss -> boss.hp -= 3 }
    )),
    Recharge(229, duration = 5, effect = Effect(
        tick = { player, _ -> player.mana += 101 }
    ))
}

private data class Effect(
    val onStart: (player: Player, boss: Player) -> Unit = { _, _ -> },
    val onEnd: (player: Player, boss: Player) -> Unit = { _, _ -> },
    val tick: (player: Player, boss: Player) -> Unit = { _, _ -> }
)

private data class Player(
    var hp: Int,
    var damage: Int = 0,
    var mana: Int = 0,
    var armor: Int = 0,
    var effects: MutableMap<Spell, Int> = mutableMapOf()
)

private fun play(
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
        return min(minMana, manaSpent)
    }

    if (playerTurn) {
        if (player.mana < Spell.entries.minOf { it.cost } || manaSpent >= minMana) return minMana

        for (spell in Spell.entries) {
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
                        newBoss,
                        newPlayer,
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
                    boss,
                    player,
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

fun main() = PuzzleInput(2015, 22).withSolutions({ part1() }, { part2() }).run()