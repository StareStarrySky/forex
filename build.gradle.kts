plugins {
    kotlin("js") version "1.6.10" apply false
    kotlin("jvm") version "1.6.10" apply false

    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    id("org.springframework.boot") version "2.6.4" apply false

    kotlin("plugin.spring") version "1.6.10"  apply false
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
    gradleVersion = "7.3.3"
    distributionType = Wrapper.DistributionType.BIN
}
