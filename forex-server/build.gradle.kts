import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        maven("https://plugins.gradle.org/m2/")
        mavenCentral()
    }

    val kotlinVersion = project.property("kotlin.version") as String
    val gradleDockerVersion = project.property("gradle.docker.version") as String

    dependencies {
        classpath(kotlin("allopen", kotlinVersion))
        classpath(kotlin("noarg", kotlinVersion))
        classpath(kotlin("gradle-plugin", kotlinVersion))
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${kotlinVersion}")
        classpath("com.bmuschko:gradle-docker-plugin:${gradleDockerVersion}")
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://www.dukascopy.com/client/jforexlib/publicrepo/")
    }

    dependencies {
        val implementation by configurations

        implementation((kotlin("reflect")))
        implementation((kotlin("stdlib")))
    }

    configure<SourceSetContainer> {
        named("main") {
            java.srcDir("src/main/kotlin")
        }
        named("test") {
            java.srcDir("src/test/kotlin")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
}
