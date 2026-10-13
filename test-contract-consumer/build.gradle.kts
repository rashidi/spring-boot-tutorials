plugins {
    java
    id("org.springframework.boot") version "4.1.1"
}

group = "zin.rashidi.boot"
version = "0.0.1-SNAPSHOT"

val springBootVersion = org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES.substringAfterLast(":")
val stubbornContractVersion = "0.1.3"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy {
        force("org.springframework.boot:spring-boot-http-client:$springBootVersion")
        force("org.springframework.boot:spring-boot-restclient:$springBootVersion")
    }
}

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    testImplementation(enforcedPlatform("sh.stubborn:stubborn-contract-dependencies:$stubbornContractVersion"))

    implementation("org.springframework.boot:spring-boot-starter-restclient")
    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testImplementation("sh.stubborn:stubborn-contract-starter-stub-runner")
    testImplementation(project(path = ":test-contract-producer", configuration = "stubs"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}