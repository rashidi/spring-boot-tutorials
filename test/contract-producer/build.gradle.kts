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
}

contracts {
    baseClassForTests.set("zin.rashidi.boot.test.contract.producer.product.ProductContractTests")
    failOnNoContracts.set(false)
}

val stubs = configurations.create("stubs") {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    add("stubs", tasks.named("verifierStubsJar"))
}
