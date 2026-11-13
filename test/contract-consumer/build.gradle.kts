import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("zin.rashidi.tutorial-conventions")
}

version = "0.0.1-SNAPSHOT"

val stubbornContractVersion = "0.2.2"

dependencies {
    testImplementation(platform("sh.stubborn:stubborn-contract-dependencies:$stubbornContractVersion"))
    testRuntimeOnly(platform("sh.stubborn:stubborn-contract-dependencies:$stubbornContractVersion"))

    implementation("org.springframework.boot:spring-boot-starter-restclient")
    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testImplementation("sh.stubborn:stubborn-contract-starter-stub-runner")
    testImplementation(project(path = ":test:contract-producer", configuration = "stubs"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
