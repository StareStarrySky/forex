plugins {
    kotlin("multiplatform") version "1.9.10" apply false
    kotlin("jvm") version "1.9.10" apply false

    id("io.spring.dependency-management") version "1.1.3" apply false
    id("org.springframework.boot") version "3.1.5" apply false

    kotlin("plugin.spring") version "1.9.10"  apply false
}

allprojects {
    group = "xyz.starestarrysky"
    version = System.getenv("PROJECT_VERSION")
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = libs.versions.gradle.asProvider().get()
    distributionType = Wrapper.DistributionType.BIN
}
