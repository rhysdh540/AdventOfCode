include(rootDir.listFiles()!!.filter {
    it.name.startsWith("Year")
}.map { it.name })

rootProject.name = "Advent Of Code"
