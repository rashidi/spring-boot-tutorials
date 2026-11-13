import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("zin.rashidi.tutorial-conventions")
    id("sh.stubborn.contract") version "0.2.2"
}

version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(platform("sh.stubborn:stubborn-contract-dependencies:0.2.2"))
    testImplementation("sh.stubborn:stubborn-contract-starter-verifier")

    contractTestImplementation(platform(SpringBootPlugin.BOM_COORDINATES))
    contractTestImplementation(platform("sh.stubborn:stubborn-contract-dependencies:0.2.2"))
}

contracts {
    baseClassForTests.set("zin.rashidi.boot.test.contract.producer.product.ProductContractTests")
    failOnNoContracts.set(false)
}
