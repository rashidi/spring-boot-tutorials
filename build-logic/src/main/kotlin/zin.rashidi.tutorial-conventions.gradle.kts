import org.gradle.api.tasks.compile.JavaCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    jacoco
}

group = "zin.rashidi.boot"

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
    runtimeOnly(platform(SpringBootPlugin.BOM_COORDINATES))
    compileOnly(platform(SpringBootPlugin.BOM_COORDINATES))
    annotationProcessor(platform(SpringBootPlugin.BOM_COORDINATES))
    testImplementation(platform(SpringBootPlugin.BOM_COORDINATES))
    testRuntimeOnly(platform(SpringBootPlugin.BOM_COORDINATES))
    testCompileOnly(platform(SpringBootPlugin.BOM_COORDINATES))
    testAnnotationProcessor(platform(SpringBootPlugin.BOM_COORDINATES))
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    finalizedBy(tasks.named("jacocoTestReport"))
}
