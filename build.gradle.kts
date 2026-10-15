import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    id("org.springframework.boot") version "4.1.1" apply false
    id("io.spring.dependency-management") version "1.1.7"
    id("jacoco-report-aggregation")
}

group = "zin.rashidi.boot"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))

    subprojects.forEach { p ->
        implementation(project(":${p.name}"))
    }
}

subprojects {
    apply(plugin = "jacoco")

    // Only configure the test task if it exists
    tasks.matching { it.name == "test" }.configureEach {
        if (this is Test) {
            finalizedBy("jacocoTestReport")
        }
    }
}

val objects = project.objects

val coverageExclusions = listOf(
    // Spring Boot @SpringBootApplication entry points and inner classes
    "**/*Application.class",
    "**/*Application$*.class",

    // Spring Boot AOT / GraalVM generated classes (if AOT is run)
    "**/*__BeanDefinitions*.class",
    "**/*__Autowiring*.class",
    "**/*__ApplicationContextInitializer*.class",

    // jOOQ generated catalog, schema, keys, and table records in :jooq
    "zin/rashidi/boot/jooq/DefaultCatalog.class",
    "zin/rashidi/boot/jooq/DefaultSchema.class",
    "zin/rashidi/boot/jooq/Indexes.class",
    "zin/rashidi/boot/jooq/Keys.class",
    "zin/rashidi/boot/jooq/Tables.class",
    "zin/rashidi/boot/jooq/tables/**"
)

val testCodeCoverageReport = tasks.named<JacocoReport>("testCodeCoverageReport") {
    val defaultClassDirs = files(classDirectories.from.toList())

    classDirectories.setFrom(
        defaultClassDirs.elements.map { locations ->
            locations.map { location ->
                objects.fileTree().from(location.asFile).matching {
                    exclude(coverageExclusions)
                }
            }
        }
    )
}

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
