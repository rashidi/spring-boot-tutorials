import nu.studer.gradle.jooq.JooqEdition
import org.jooq.meta.kotlin.*

plugins {
    java
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("nu.studer.jooq") version "10.1"
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
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    jooqGenerator("org.jooq:jooq-meta-extensions")
    jooqGenerator("com.mysql:mysql-connector-j")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
}

dependencyManagement {
    dependencies {
        dependency("org.jooq:jooq-meta-extensions:${dependencyManagement.importedProperties["jooq.version"]}")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

jooq {
    version.set(dependencyManagement.importedProperties["jooq.version"])
    edition.set(JooqEdition.OSS)

    configurations {
        create("main") {

            jooqConfiguration {
                generator {
                    database {
                        name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                        properties {
                            property {
                                key = "scripts"
                                value = "src/main/resources/mysql-schema.sql"
                            }
                        }
                    }
                    target {
                        packageName = "zin.rashidi.boot.jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }

        }
    }
}