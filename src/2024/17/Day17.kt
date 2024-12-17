import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.analysis.AnalyzerException
import org.objectweb.asm.util.CheckClassAdapter
import java.io.PrintWriter
import java.io.Writer
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeBytes

fun part1_17(input: String): Any? {
    val parts = input.split("\n\n")
    val registers = parts[0].lines().associate {
        val (r, v) = Regex("""Register (.): (\d+)""").find(it)!!.destructured
        r to v.toLong()
    }.toMutableMap()

    val instructions = parts[1].drop("Program: ".length).split(",").map { it.toInt() }

    val t = Triple(registers["A"]!!, registers["B"]!!, registers["C"]!!)
    val output = Day17.runCompiled(t, instructions)
    return output.joinToString(",")
}

fun part2_17(input: String): Any? {
    val parts = input.split("\n\n")
    val instructions = parts[1].drop("Program: ".length).split(",").map { it.toInt() }

    var a = 0L
    for (i in instructions.indices.reversed()) {
        a = a shl 3 // move to the left by 3 bits to make room for the next value
        val shortenedProgram = instructions.drop(i)
        while (Day17.interpret(Triple(a, 0, 0), instructions) != shortenedProgram) {
            a++
        }
    }

    if (Day17.interpret(Triple(a, 0, 0), instructions) != instructions) {
        error("Reconstructed A value: $a is incorrect")
    }

    return a
}

object Day17 {
    fun interpret(registers: Triple<Long, Long, Long>, instructions: List<Int>): List<Int> {
        var (a, b, c) = registers

        fun getComboValue(operand: Int): Long {
            return when (operand) {
                in 0..3 -> operand.toLong()
                4 -> a
                5 -> b
                6 -> c
                else -> error("Invalid combo operand: $operand")
            }
        }

        var i = 0
        val output = mutableListOf<Int>()
        while (i < instructions.size) {
            if (i + 1 >= instructions.size) break

            val opcode = instructions[i]
            val operand = instructions[i + 1]

            when (opcode) {
                0 -> { // adv
                    a = a shr getComboValue(operand).toInt()
                }
                1 -> { // bxl
                    b = b xor operand.toLong()
                }
                2 -> { // bst
                    b = getComboValue(operand) % 8
                }
                3 -> { // jnz
                    if (a != 0L) {
                        i = operand
                        continue
                    }
                }
                4 -> { // bxc
                    b = b xor c
                }
                5 -> { // out
                    output.add((getComboValue(operand) % 8).toInt())
                }
                6 -> { // bdv
                    b = a shr getComboValue(operand).toInt()
                }
                7 -> { // cdv
                    c = a shr getComboValue(operand).toInt()
                }
                else -> error("Unknown opcode: $opcode at position $i")
            }

            i += 2
        }

        return output
    }

    private val compileCache = mutableMapOf<List<Int>, MethodHandle>()

    fun runCompiled(registers: Triple<Long, Long, Long>, instructions: List<Int>): List<Int> {
        @Suppress("UNCHECKED_CAST")
        return compile(instructions).invoke(registers.first, registers.second, registers.third) as List<Int>
    }

    data class CompilerOptions(
        val optimizeMod: Boolean = true,
        val optimizeDiv: Boolean = true,
        val variableNames: Boolean = true,
        val invalidateCache: Boolean = false,
        val debug: Boolean = false,
    )

    fun compile(program: List<Int>, opts: CompilerOptions = CompilerOptions()): MethodHandle {
        if(opts.invalidateCache) {
            compileCache.remove(program)
        }
        compileCache[program]?.let { return it }

        @OptIn(ExperimentalStdlibApi::class)
        val name = "\$\$Program\$\$${program.hashCode().toHexString()}"

        val w = ClassWriter(ClassWriter.COMPUTE_MAXS)
        w.visit(V1_5, ACC_PUBLIC, name, null, "java/lang/Object", null)
        val m = w.visitMethod(ACC_PUBLIC or ACC_STATIC, "run", "(JJJ)Ljava/util/ArrayList;", null, null)

        // longs take 2 lvt indexes
        val (lvtA, lvtB, lvtC, lvtOutput) = listOf(0, 2, 4, 6)

        // initialize output
        m.visitTypeInsn(NEW, "java/util/ArrayList")
        m.visitInsn(DUP)
        m.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false)
        m.visitVarInsn(ASTORE, lvtOutput)

        // registers are initialized as arguments

        /**
         * Get the operand for the combo instruction
         * @param int if the thing loaded onto the stack should be an int (if false, it will be a long)
         */
        fun getComboOperand(operand: Int, int: Boolean = false) {
            when (operand) {
                in 0..3 -> m.visitLdcInsn(if(int) operand else operand.toLong())
                4 -> m.visitVarInsn(LLOAD, lvtA)
                5 -> m.visitVarInsn(LLOAD, lvtB)
                6 -> m.visitVarInsn(LLOAD, lvtC)
                else -> error("Invalid combo operand: $operand")
            }

            if(operand !in 0..3 && int) {
                m.visitInsn(L2I)
            }
        }

        // shared logic for adv, bdv, and cdv
        // store the result of a / (1 << combo(operand)) or a >> combo(operand) into storeIndex
        fun div(storeIndex: Int, operand: Int) {
            m.visitVarInsn(LLOAD, lvtA)
            if(opts.optimizeDiv) {
                // a = a >> combo(operand)
                getComboOperand(operand, true)
                m.visitInsn(LSHR)
            } else {
                // a = a / (1 << combo(operand))
                m.visitInsn(LCONST_1)
                getComboOperand(operand, true)
                m.visitInsn(LSHL)
                m.visitInsn(LDIV)
            }
            m.visitVarInsn(LSTORE, storeIndex)
        }

        val labels = Array(program.size / 2) { Label() }

        if(program.size % 2 != 0) {
            error("Program size must be even")
        }

        for(i in program.chunked(2).withIndex()) {
            val (insn, operand) = i.value
            m.visitLabel(labels[i.index])

            if(opts.debug) {
                println("Insn: $insn, Operand: $operand, Label: L${i.index}")
            }

            when (insn) {
                0 -> { // adv
                    div(lvtA, operand)
                }
                1 -> { // bxl
                    // b = b ^ operand

                    m.visitVarInsn(LLOAD, lvtB)
                    m.visitLdcInsn(operand.toLong())
                    m.visitInsn(LXOR)
                    m.visitVarInsn(LSTORE, lvtB)
                }
                2 -> { // bst
                    // b = combo(operand) % 8

                    getComboOperand(operand)
                    if(opts.optimizeMod) {
                        m.visitLdcInsn(7L)
                        m.visitInsn(LAND)
                    } else {
                        m.visitLdcInsn(8L)
                        m.visitInsn(LREM)
                    }
                    m.visitVarInsn(LSTORE, lvtB)
                }
                3 -> { // jnz
                    // if a != 0 goto operand

                    m.visitVarInsn(LLOAD, lvtA)
                    m.visitInsn(LCONST_0)
                    m.visitInsn(LCMP)
                    m.visitJumpInsn(IFNE, labels[operand])
                }
                4 -> { // bxc
                    // b = b ^ c

                    m.visitVarInsn(LLOAD, lvtB)
                    m.visitVarInsn(LLOAD, lvtC)
                    m.visitInsn(LXOR)
                    m.visitVarInsn(LSTORE, lvtB)
                }
                5 -> { // out
                    // combo(operand) % 8

                    m.visitVarInsn(ALOAD, lvtOutput)
                    getComboOperand(operand)
                    if(opts.optimizeMod) {
                        m.visitLdcInsn(7L)
                        m.visitInsn(LAND)
                    } else {
                        m.visitLdcInsn(8L)
                        m.visitInsn(LREM)
                    }

                    // output.add(Integer.valueOf((int) combo(operand) % 8))

                    m.visitInsn(L2I)
                    m.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
                    m.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false)
                    m.visitInsn(POP)
                }
                6 -> { // bdv
                    div(lvtB, operand)
                }
                7 -> { // cdv
                    div(lvtC, operand)
                }
                else -> error(buildString {
                    val index = i.index * 2
                    append("Unknown opcode: $insn at position ${index + 1}\n")
                    if(program.size <= 16) {
                        append(program.joinToString(","))
                        append("\n")
                        append("  ".repeat(index))
                        append("^")
                    } else {
                        val start = maxOf(0, index - 8)
                        val end = minOf(program.size, index + 8)
                        append(program.subList(start, end).joinToString(","))
                        append("\n")
                        append("  ".repeat(index - start))
                        append("^")
                    }
                })
            }
        }

        m.visitVarInsn(ALOAD, lvtOutput)
        m.visitInsn(ARETURN)

        if(opts.variableNames) {
            m.visitParameter("a", 0)
            m.visitParameter("b", 0)
            m.visitParameter("c", 0)
            m.visitLocalVariable("output", "Ljava/util/ArrayList;", null, labels[0], labels.last(), lvtOutput)
        }

        m.visitMaxs(0, 0) // hardcoded, might be wrong for some programs?
        m.visitEnd()
        w.visitEnd()

        // load the class
        val cl = object : ClassLoader() {
            fun defineClass(name: String, bytes: ByteArray): Class<*> {
                return defineClass(name, bytes, 0, bytes.size)
            }
        }

        val bytes = w.toByteArray()

        if(opts.debug) {
            verify(bytes)
            if (!Path("testing").exists()) Path("testing").createDirectory()
            Path("testing/$name.class").writeBytes(bytes)
        }

        val clazz = cl.defineClass(name, bytes)
        val method = clazz.getMethod("run", Long::class.java, Long::class.java, Long::class.java)
        val handle = MethodHandles.lookup().unreflect(method)

        compileCache[program] = handle
        return handle
    }

    fun verify(clazz: ByteArray) {
        try {
            CheckClassAdapter.verify(ClassReader(clazz), false, PrintWriter(Writer.nullWriter()))
        } catch (_: AnalyzerException) {
            CheckClassAdapter.verify(ClassReader(clazz), true, PrintWriter(System.err))
        }
    }
}

fun main() {
    val input = Path("inputs/2024/17.txt").readText()

    var start = System.nanoTime()
    var result = part1_17(input)
    var end = System.nanoTime()
    println("--- Part 1: %.2fms ---".format((end - start) / 1e6))
    println(result)

    start = System.nanoTime()
    result = part2_17(input)
    end = System.nanoTime()
    println("--- Part 2: %.2fms ---".format((end - start) / 1e6))
    println(result)
    println("----------------------")
}