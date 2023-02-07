plugins {
    kotlin("js") version "1.8.0" apply false
    kotlin("jvm") version "1.8.0" apply false

    id("io.spring.dependency-management") version "1.1.0" apply false
    id("org.springframework.boot") version "3.0.2" apply false

    kotlin("plugin.spring") version "1.8.0"  apply false
}

allprojects {
    group = "xyz.starestarrysky"
    version = System.getenv("PROJECT_VERSION")
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = libs.versions.gradle.asProvider().get()
    distributionType = Wrapper.DistributionType.BIN
}
