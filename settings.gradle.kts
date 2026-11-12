pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "spring-boot-tutorials"

fileTree(rootDir) {
    include("*/*/build.gradle.kts")
    exclude("build-logic/**", ".gradle/**", "build/**")
}.files.sortedBy { it.relativeTo(rootDir).invariantSeparatorsPath }.forEach {
    include(":${it.parentFile.relativeTo(rootDir).invariantSeparatorsPath.replace('/', ':')}")
}