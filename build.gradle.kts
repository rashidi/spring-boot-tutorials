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

    implementation(project(":batch-rest-repository"))
    implementation(project(":batch-skip-step"))
    implementation(project(":cloud-jdbc-env-repo"))
    implementation(project(":data-domain-events"))
    implementation(project(":data-envers-audit"))
    implementation(project(":data-jdbc-audit"))
    implementation(project(":data-jdbc-optimistic-locking"))
    implementation(project(":data-jdbc-schema-generation"))
    implementation(project(":data-jpa-audit"))
    implementation(project(":data-jpa-event"))
    implementation(project(":data-jpa-filtered-query"))
    implementation(project(":data-jpa-hibernate-cache"))
    implementation(project(":data-mongodb-audit"))
    implementation(project(":data-mongodb-full-text-search"))
    implementation(project(":data-mongodb-tc-data-load"))
    implementation(project(":data-mongodb-transactional"))
    implementation(project(":data-redis-cache"))
    implementation(project(":data-repository-definition"))
    implementation(project(":data-rest-composite-id"))
    implementation(project(":data-rest-validation"))
    implementation(project(":graphql"))
    implementation(project(":jooq"))
    implementation(project(":modulith"))
    implementation(project(":modulith-events"))
    implementation(project(":test-execution-listeners"))
    implementation(project(":test-rest-assured"))
    implementation(project(":test-slice-tests-rest"))
    implementation(project(":web-rest-client"))
    implementation(project(":web-thymeleaf-xss"))

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
