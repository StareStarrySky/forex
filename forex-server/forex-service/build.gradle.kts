import com.bmuschko.gradle.docker.tasks.image.*

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("io.spring.dependency-management")
    id("org.springframework.boot")
    id("com.bmuschko.docker-remote-api")
}

dependencies {
    implementation(project(":forex-server:forex-JForex"))

    implementation("org.springframework.boot:spring-boot-starter")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
//    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation("org.springframework.security.oauth:spring-security-oauth2:2.2.2.RELEASE")
//    implementation("org.springframework.boot:spring-boot-starter-websocket")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    compileOnly("org.springframework.boot:spring-boot-devtools")
//    implementation("mysql:mysql-connector-java:8.0.18")
//    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5")
}

tasks {
    jar {
        enabled = true
    }

    bootJar {
        archiveFileName.value("server.jar")
        archiveClassifier.value("boot")
    }

    docker {
        url.value(System.getenv("DOCKER_URL"))
        registryCredentials {
            username.value(System.getenv("DOCKER_USERNAME"))
            password.value(System.getenv("DOCKER_PASSWORD"))
        }
    }
}

tasks.register<DockerBuildImage>("buildImage") {
    dependsOn("bootJar")
    inputDir.set(file("."))
    images.add("${System.getenv("DOCKER_IMAGE")}:${version}")
}

tasks.register<DockerPushImage>("pushImage") {
    dependsOn("buildImage")
    images.add("${System.getenv("DOCKER_IMAGE")}:${version}")
}

tasks.register<DockerRemoveImage>("removeImage") {
    imageId.value("${System.getenv("DOCKER_IMAGE")}:${version}")
}
