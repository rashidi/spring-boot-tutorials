plugins {
	java
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
}

description = "Testcontainers Server Tutorial"

repositories {
    mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.matching { it.name == "bootBuildImage" }.configureEach {
    val bootBuildImage = this as org.springframework.boot.gradle.tasks.bundling.BootBuildImage
    bootBuildImage.imageName.set("ghcr.io/rashidi/testcontainers-demo-server:latest")
    bootBuildImage.docker {
        publishRegistry {
            username.set(System.getenv("GITHUB_ACTOR"))
            password.set(System.getenv("GITHUB_TOKEN"))
            url.set("https://ghcr.io")
        }
    }
}

tasks.withType<Test> {
	useJUnitPlatform()
}
