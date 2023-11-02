plugins {
    id("maven-publish")
    id("org.jetbrains.dokka")
    kotlin("jvm")
}

dependencies {
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
