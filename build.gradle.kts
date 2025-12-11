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

    implementation(kotlin("reflect"))
}

kotlin {
    jvmToolchain(25)
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs = listOf("-Xmx2G", "-XX:+UseZGC", "-XX:+UseCompactObjectHeaders")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.freeCompilerArgs.add("-Xcontext-parameters")
}
