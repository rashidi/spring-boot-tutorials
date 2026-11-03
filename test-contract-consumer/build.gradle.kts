plugins {
    java
    id("org.springframework.boot") version "4.1.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "zin.rashidi.boot"
version = "0.0.1-SNAPSHOT"

val stubbornContractVersion = "0.2.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("sh.stubborn:stubborn-contract-dependencies:$stubbornContractVersion"))

    implementation("org.springframework.boot:spring-boot-starter-restclient")
    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testImplementation("sh.stubborn:stubborn-contract-starter-stub-runner")
    testImplementation(project(path = ":test-contract-producer", configuration = "stubs"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}