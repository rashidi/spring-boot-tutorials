plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "zin.rashidi.boot"
version = "0.0.1-SNAPSHOT"

val springCloudVersion = "2025.1.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"))

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-config-server")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-resttestclient")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-mysql")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}