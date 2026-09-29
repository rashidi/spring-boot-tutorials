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

    jacocoAggregation(project(":batch-rest-repository"))
    jacocoAggregation(project(":batch-skip-step"))
    jacocoAggregation(project(":cloud-jdbc-env-repo"))
    jacocoAggregation(project(":data-domain-events"))
    jacocoAggregation(project(":data-envers-audit"))
    jacocoAggregation(project(":data-jdbc-audit"))
    jacocoAggregation(project(":data-jdbc-optimistic-locking"))
    jacocoAggregation(project(":data-jdbc-schema-generation"))
    jacocoAggregation(project(":data-jpa-audit"))
    jacocoAggregation(project(":data-jpa-event"))
    jacocoAggregation(project(":data-jpa-filtered-query"))
    jacocoAggregation(project(":data-jpa-hibernate-cache"))
    jacocoAggregation(project(":data-mongodb-audit"))
    jacocoAggregation(project(":data-mongodb-full-text-search"))
    jacocoAggregation(project(":data-mongodb-tc-data-load"))
    jacocoAggregation(project(":data-mongodb-transactional"))
    jacocoAggregation(project(":data-redis-cache"))
    jacocoAggregation(project(":data-repository-definition"))
    jacocoAggregation(project(":data-rest-composite-id"))
    jacocoAggregation(project(":data-rest-validation"))
    jacocoAggregation(project(":graphql"))
    jacocoAggregation(project(":jooq"))
    jacocoAggregation(project(":modulith"))
    jacocoAggregation(project(":modulith-events"))
    jacocoAggregation(project(":test-execution-listeners"))
    jacocoAggregation(project(":test-rest-assured"))
    jacocoAggregation(project(":test-slice-tests-rest"))
    jacocoAggregation(project(":web-rest-client"))
    jacocoAggregation(project(":web-thymeleaf-xss"))
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}
