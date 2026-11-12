plugins {
    id("zin.rashidi.tutorial-conventions")
}

version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
