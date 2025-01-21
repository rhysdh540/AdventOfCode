package dev.rdh.aoc.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.net.HttpURLConnection

abstract class DownloadInputTask : DefaultTask() {
    @TaskAction
    fun downloadInput() {
        for(year in 2015..now.year) {
            if(project.sourceSets.findByName("$year") == null) continue
            for(day in 1..25) {
                // make sure it's not in the future
                if(year == now.year && day > now.dayOfMonth) break
                project.dl(year, day)
            }
        }
    }
}

fun Project.dl(year: Int, day: Int) {
    val file = file("src/$year/resources/$day.txt")
    if(file.exists() && file.length() != 0L) return
    file.parentFile.mkdirs()
    val url = uri("https://adventofcode.com/$year/day/$day/input").toURL()
    val conn = url.openConnection() as HttpURLConnection
    conn.setRequestProperty("Cookie", "session=${aocToken}")
    conn.connect()

    // for some reason it comes with a trailing newline
    val content = String(conn.inputStream.readAllBytes()).removeSuffix("\n")
    file.writeText(content)
    logger.lifecycle("Downloaded input for $year day $day")
}

val Project.aocToken: String get() {
    file("token.txt").let {
        if(it.exists()) return it.readText().trim()
    }

    for(name in listOf("AOC_TOKEN", "AOC_SESSION")) {
        val token = System.getenv(name)
        if(token != null) return token
    }

    for(name in listOf("aocToken", "aocSession")) {
        val token = project.findProperty(name) as? String
        if(token != null) return token
    }

    error("""
        error: no token found
        Please provide your Advent of Code session token in a file named 'token.txt'
        or as an environment variable named 'AOC
    """.trimIndent())
}