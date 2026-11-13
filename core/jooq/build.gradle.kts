import nu.studer.gradle.jooq.JooqEdition
import org.jooq.meta.kotlin.*
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("zin.rashidi.tutorial-conventions")
    id("nu.studer.jooq") version "10.2.1"
}

version = "0.0.1-SNAPSHOT"

val jooqVersion = "3.20.8"

dependencies {
    jooqGenerator(platform(SpringBootPlugin.BOM_COORDINATES))

    implementation("org.springframework.boot:spring-boot-starter-jooq")
    jooqGenerator("org.jooq:jooq-meta-extensions:$jooqVersion")
    jooqGenerator("com.mysql:mysql-connector-j")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-jooq-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-mysql")
}

jooq {
    version.set(jooqVersion)
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
