import dev.rdh.aoc.*

const val print = false

private fun PuzzleInput.part1(): Any? {
    // maps name to (module, outputs)
    val modules = lines.associate {
        val (fullName, outputs) = it.split(" -> ")
        val name = fullName.removePrefix("%").removePrefix("&")
        val module = when {
            fullName[0] == '%' -> FlipFlop()
            fullName[0] == '&' -> Conjunction()
            fullName == "broadcaster" -> Broadcaster
            else -> error("Unknown module type: $fullName")
        }

        name to (module to outputs.split(", "))
    }

    // set up conjunction inputs
    modules.forEach { (name, pair) ->
        val (module, _) = pair
        if (module is Conjunction) {
            modules.forEach { (otherName, otherPair) ->
                val (_, otherOutputs) = otherPair
                if (otherOutputs.contains(name)) {
                    module.addInput(otherName)
                }
            }
        }
    }

    // sender to signal
    val signalsToProcess = ArrayDeque<Signal>()

    var lowSent = 0L
    var highSent = 0L

    fun buttonPush() {
        lowSent++ // button push is always low
        if (print) println("button -low-> broadcaster")
        modules["broadcaster"]!!.second.forEach {
            signalsToProcess += Signal("broadcaster", false, it)
        }

        while (signalsToProcess.isNotEmpty()) {
            val (senderName, signal, receiverName) = signalsToProcess.removeFirst()
            if (signal) highSent++ else lowSent++
            if (print) println("$senderName -${if (signal) "high" else "low"}-> $receiverName")
            val (receiverModule, outputs) = modules[receiverName] ?: continue
            val newSignal = receiverModule.accept(senderName, signal)
            if (newSignal != null) {
                outputs.forEach {
                    signalsToProcess += Signal(receiverName, newSignal, it)
                }
            }
        }
    }

    repeat(1000) {
        buttonPush()
    }

    return lowSent * highSent
}


private fun PuzzleInput.part2(): Any? {
    val modules = lines.associate {
        val (fullName, outputs) = it.split(" -> ")
        val name = fullName.removePrefix("%").removePrefix("&")
        val module = when {
            fullName[0] == '%' -> FlipFlop()
            fullName[0] == '&' -> Conjunction()
            fullName == "broadcaster" -> Broadcaster
            else -> error("Unknown module type: $fullName")
        }
        name to (module to outputs.split(", "))
    }

    modules.forEach { (name, pair) ->
        val (module, _) = pair
        if (module is Conjunction) {
            modules.forEach { (otherName, otherPair) ->
                val (_, otherOutputs) = otherPair
                if (otherOutputs.contains(name)) {
                    module.addInput(otherName)
                }
            }
        }
    }

    // Identify the single input conjunction feeding rx
    val rxParentName = modules.entries.first { (_, v) -> v.second.contains("rx") }.key
    val rxParent = modules[rxParentName]!!.first as? Conjunction
        ?: error("Parent of rx is not a conjunction")
    val targets = rxParent.inputs.toSet() // names of modules that send into rxParent

    val queue = ArrayDeque<Signal>()

    // Record first button press when each target sender sends a HIGH to rxParent
    val firstHighFromSender = mutableMapOf<String, Long>()

    var pushes = 0L
    while (firstHighFromSender.size < targets.size) {
        pushes++
        // Button press: broadcaster sends LOW to its outputs
        modules["broadcaster"]!!.second.forEach {
            queue += Signal("broadcaster", false, it)
        }

        while (queue.isNotEmpty() && firstHighFromSender.size < targets.size) {
            val (sender, sig, receiver) = queue.removeFirst()

            // if signal is high and receiver conjunction feeding rx, record this cycle
            if (receiver == rxParentName && sig && sender in targets && sender !in firstHighFromSender) {
                firstHighFromSender[sender] = pushes
            }

            val (receiverModule, outputs) = modules[receiver] ?: continue
            val outSig = receiverModule.accept(sender, sig) ?: continue
            outputs.forEach {
                queue += Signal(receiver, outSig, it)
            }
        }
    }

    return firstHighFromSender.values.reduce(Long::lcm)
}

private data class Signal(val sender: String, val signal: Boolean, val receiver: String)

private interface Module {
    // accepts a low or high signal (false/true) and returns the kind of signal to send to its outputs
    fun accept(sender: String, signal: Boolean): Boolean?
}

private class FlipFlop : Module {
    private var state = false
    override fun accept(sender: String, signal: Boolean): Boolean? {
        if (!signal) {
            state = !state
            return state
        }
        return null
    }
}

private class Conjunction : Module {
    private val states = mutableMapOf<String, Boolean>()
    val inputs: Set<String> get() = states.keys

    fun addInput(name: String) {
        states[name] = false
    }

    override fun accept(sender: String, signal: Boolean): Boolean? {
        states[sender] = signal
        return !states.values.all { it }
    }
}

private object Broadcaster : Module {
    override fun accept(sender: String, signal: Boolean): Boolean? = signal
}

fun main() {
    val input = getInput(2023, 20)

    var start = System.nanoTime()
    var result = input.part1()
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = input.part2()
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}