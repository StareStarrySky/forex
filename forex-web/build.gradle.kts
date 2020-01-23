plugins {
    kotlin("js")
}

val kotlinReactVersion = project.property("kotlin.react.version") as String
val kotlinReactDomVersion = project.property("kotlin.react.dom.version") as String
val kotlinStyledVersion = project.property("kotlin.styled.version") as String

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:${kotlinReactVersion}")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:${kotlinReactDomVersion}")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:${kotlinStyledVersion}")
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}
