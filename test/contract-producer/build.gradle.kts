import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("zin.rashidi.tutorial-conventions")
    id("sh.stubborn.contract") version "0.2.2"
}

version = "0.0.1-SNAPSHOT"

val stubbornContractVersion = "0.2.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(platform("sh.stubborn:stubborn-contract-dependencies:$stubbornContractVersion"))
    testImplementation("sh.stubborn:stubborn-contract-starter-verifier")
}

configurations.matching { it.name.startsWith("contractTest") }.configureEach {
    dependencies.add(project.dependencies.platform(SpringBootPlugin.BOM_COORDINATES))
    dependencies.add(project.dependencies.platform("sh.stubborn:stubborn-contract-dependencies:$stubbornContractVersion"))
}

contracts {
    baseClassForTests.set("zin.rashidi.boot.test.contract.producer.product.ProductContractTests")
    failOnNoContracts.set(false)
}
