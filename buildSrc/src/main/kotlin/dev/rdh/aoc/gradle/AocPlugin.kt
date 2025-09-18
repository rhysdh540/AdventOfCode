package dev.rdh.aoc.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import java.time.LocalDate
import java.time.ZoneId
import org.gradle.kotlin.dsl.*

class AocPlugin : Plugin<Project> {
    override fun apply(project: Project) = project.applyPlugin()

    private fun Project.applyPlugin() {
        for (year in 2015..now.year) {
            if (file("src/$year").exists()) {
                sourceSets.create("$year").apply {
                    kotlin.srcDir("src/$year/kotlin")
                    compileClasspath += sourceSets["main"].output
                    runtimeClasspath += sourceSets["main"].output
                }
            }
        }

        tasks.register("aocInit", InitTask::class.java) {
            group = "aoc"
            description = "Initialize a new Advent of Code puzzle and/or download its input"
        }
    }
}

val now: LocalDate = LocalDate.now(ZoneId.of("EST", ZoneId.SHORT_IDS))
val Project.sourceSets get() = project.extensions.getByType(SourceSetContainer::class.java)
val SourceSet.kotlin get() = extensions.getByName("kotlin") as SourceDirectorySet