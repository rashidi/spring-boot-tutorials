plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("sh.stubborn.contract") version "0.1.3"
    `maven-publish`
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
    testImplementation("sh.stubborn:stubborn-contract-starter-verifier")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

contracts {
    baseClassForTests.set("zin.rashidi.boot.test.contract.producer.BaseContractTest")
    failOnNoContracts.set(false)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(tasks.named("verifierStubsJar"))
        }
    }
}

tasks.withType<org.gradle.api.publish.tasks.GenerateModuleMetadata>().configureEach {
    suppressedValidationErrors.add("dependencies-without-versions")
}
