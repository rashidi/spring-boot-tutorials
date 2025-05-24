import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    id("org.springframework.boot") version "3.5.0" apply false
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "6.2.0.5505"
    id("jacoco-report-aggregation")
}

group = "zin.rashidi.boot"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    subprojects.forEach { p ->
        implementation(project(":${p.name}"))
    }
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
}

sonar {
    properties {
        property("sonar.projectKey", "rashidi_spring-boot-tutorials")
        property("sonar.organization", "rashidi-github")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", "${layout.buildDirectory.get()}/reports/jacoco/testCodeCoverageReport/testCodeCoverageReport.xml")
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

