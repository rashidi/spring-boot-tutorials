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

val coverageVerification = tasks.register<JacocoCoverageVerification>("testCodeCoverageVerification") {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
    violationRules {
        rule {
            limit {
                counter = "INSTRUCTION"
                minimum = "0.90".toBigDecimal()
            }
        }
    }
    executionData(
        fileTree(layout.buildDirectory.dir("jacoco")).matching { include("**/*.exec") }
    )
    classDirectories.setFrom(
        subprojects.map { p -> fileTree("${p.projectDir}/build/classes/java/main") }
    )
    sourceDirectories.setFrom(
        subprojects.map { p -> fileTree("${p.projectDir}/src/main/java") }
    )
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
    dependsOn(coverageVerification)
}