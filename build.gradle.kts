import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.0"
}

apply<dev.rdh.aoc.gradle.AocPlugin>()

repositories {
    mavenCentral()
}

dependencies {
    "2024Implementation"("org.ow2.asm:asm-tree:9.7.1")
    "2024Implementation"("org.ow2.asm:asm-util:9.7.1")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<KotlinCompile> {
    compilerOptions.freeCompilerArgs.add("-Xcontext-receivers")
}
