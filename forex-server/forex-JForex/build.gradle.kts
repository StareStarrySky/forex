//import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("maven-publish")
    id("org.jetbrains.dokka")
    kotlin("jvm")
}

val jForexAPIVersion = project.property("JForex-API.version") as String
val dds2JClientJForexVersion = project.property("DDS2-jClient-JForex.version") as String

dependencies {
    api(project(":forex-server:forex-base"))

    api("com.dukascopy.api:JForex-API:${jForexAPIVersion}:sources")
    api("com.dukascopy.dds2:DDS2-jClient-JForex:${dds2JClientJForexVersion}") {
        // for springboot mail 3.0.2
        exclude("javax.activation", "activation")
    }
}

val generateSourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().java.srcDirs)
}

//tasks.register<DokkaTask>("dokkaJavadoc") {
//    outputFormat = "javadoc"
//    outputDirectory = "$buildDir" + File.separator + "javadoc"
//}

val generateJavadoc by tasks.creating(Jar::class) {
    dependsOn("dokkaJavadoc")
    group = "jar"
    archiveClassifier.set("javadoc")
    from(tasks["dokkaJavadoc"].property("outputDirectory"))
}

tasks {
    publishing {
        repositories {
            maven {
//                name = "nexus-releases"
//                url = uri("")
//                credentials {
//                    username = ""
//                    password = ""
//                }
            }
        }
        publications {
            create<MavenPublication>("maven") {
                artifact(generateJavadoc)
                artifact(generateSourcesJar)

//                afterEvaluate {
//                    artifactId = tasks.jar.get().archiveBaseName.get()
//                }
                from(getComponents()["java"])
            }
        }
    }
}
