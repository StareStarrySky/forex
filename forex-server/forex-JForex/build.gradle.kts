plugins {
    id("maven-publish")
    id("org.jetbrains.dokka")
    kotlin("jvm")
}

dependencies {
    api(project(":forex-server:forex-base"))

    api("com.dukascopy.api:JForex-API:${libs.versions.jforex.api.get()}:sources")
    api(libs.dds2.jClient.jForex) {
        // for springboot mail 3.0.2
        exclude("javax.activation", "activation")
    }
}

val generateSourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().java.srcDirs)
}

tasks.dokkaJavadoc {
    outputDirectory.set(layout.buildDirectory.dir("dokka/$name"))
}

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
