import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    id("org.springframework.boot") version "4.1.2" apply false
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

        // jOOQ generated catalog, schema, keys, and table records in :jooq
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

tasks.register<JacocoCoverageVerification>("testCodeCoverageVerification") {
    val reportTask = tasks.named<JacocoReport>("testCodeCoverageReport").get()
    dependsOn(reportTask)

    executionData.setFrom(reportTask.executionData)
    classDirectories.setFrom(reportTask.classDirectories)
    sourceDirectories.setFrom(reportTask.sourceDirectories)

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
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
    dependsOn(tasks.named<JacocoCoverageVerification>("testCodeCoverageVerification"))
}
