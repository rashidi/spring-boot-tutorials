plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
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
    maven {
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
        mavenContent {
            snapshotsOnly()
        }
    }
}

dependencyManagement {
    imports {
        mavenBom("sh.stubborn:stubborn-contract-dependencies:0.1.3")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("sh.stubborn:stubborn-contract-starter-stub-runner")
    testImplementation(project(path = ":test-contract-producer", configuration = "stubs"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
