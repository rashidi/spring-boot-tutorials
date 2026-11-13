plugins {
    base
    id("jacoco-report-aggregation")
}

group = "zin.rashidi.boot"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    fileTree(rootDir) {
        include("*/*/build.gradle.kts")
        exclude("build-logic/**", ".gradle/**", "build/**")
    }.files.sortedBy { it.relativeTo(rootDir).invariantSeparatorsPath }.forEach {
        jacocoAggregation(project(":${it.parentFile.relativeTo(rootDir).invariantSeparatorsPath.replace('/', ':')}"))
    }
}

tasks.named<JacocoReport>("testCodeCoverageReport") {
    val objectFactory = project.objects
    val exclusions = listOf(
        // Spring Boot @SpringBootApplication entry points and inner classes
        "**/*Application.class",
        "**/*Application$*.class",

        // Spring Boot AOT / GraalVM generated classes (if AOT is run)
        "**/*__BeanDefinitions*.class",
        "**/*__Autowiring*.class",
        "**/*__ApplicationContextInitializer*.class",

        // jOOQ generated catalog, schema, keys, and table records in :core:jooq
        "zin/rashidi/boot/jooq/DefaultCatalog.class",
        "zin/rashidi/boot/jooq/DefaultSchema.class",
        "zin/rashidi/boot/jooq/Indexes.class",
        "zin/rashidi/boot/jooq/Keys.class",
        "zin/rashidi/boot/jooq/Tables.class",
        "zin/rashidi/boot/jooq/tables/**"
    )
    val defaultClassDirs = objectFactory.fileCollection().from(classDirectories.from.toList())

    classDirectories.setFrom(
        defaultClassDirs.elements.map { locations ->
            locations.map { location ->
                objectFactory.fileTree().from(location.asFile).matching {
                    exclude(exclusions)
                }
            }
        }
    )
}

val testCodeCoverageReport = tasks.named<JacocoReport>("testCodeCoverageReport")

val testCodeCoverageVerification = tasks.register<JacocoCoverageVerification>("testCodeCoverageVerification") {
    dependsOn(testCodeCoverageReport)

    executionData.setFrom(testCodeCoverageReport.map { it.executionData })
    classDirectories.setFrom(testCodeCoverageReport.map { it.classDirectories })
    sourceDirectories.setFrom(testCodeCoverageReport.map { it.sourceDirectories })

    violationRules {
        rule {
            limit {
                counter = "INSTRUCTION"
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(testCodeCoverageReport)
    dependsOn(testCodeCoverageVerification)
}
