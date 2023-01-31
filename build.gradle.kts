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

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://www.dukascopy.com/client/jforexlib/publicrepo/")
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.6"
    distributionType = Wrapper.DistributionType.BIN
}
