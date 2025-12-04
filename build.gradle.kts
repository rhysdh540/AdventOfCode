import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.0-RC"
}

apply<dev.rdh.aoc.gradle.AocPlugin>()

repositories {
    mavenCentral()
}

dependencies {
    "2024Implementation"("org.ow2.asm:asm-tree:9.9")
    "2024Implementation"("org.ow2.asm:asm-util:9.9")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

kotlin {
    jvmToolchain(25)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.freeCompilerArgs.add("-Xcontext-parameters")
}
