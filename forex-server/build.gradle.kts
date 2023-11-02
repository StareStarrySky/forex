import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("allopen", libs.versions.kotlin.asProvider().get()))
        classpath(kotlin("noarg", libs.versions.kotlin.asProvider().get()))
        classpath(kotlin("gradle-plugin", libs.versions.kotlin.asProvider().get()))
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${libs.versions.dokka.gradle.get()}")
        classpath("com.bmuschko:gradle-docker-plugin:${libs.versions.gradle.docker.bmuschko.get()}")
    }
}

subprojects {
    val javaVersion = project.property("java.version") as String

    apply(plugin = "java-library")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        val implementation by configurations

        implementation((kotlin("reflect")))
        implementation((kotlin("stdlib")))
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.toVersion(javaVersion)
        targetCompatibility = JavaVersion.toVersion(javaVersion)
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
            jvmTarget = javaVersion
        }
    }
}
