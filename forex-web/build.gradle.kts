plugins {
    kotlin("multiplatform")
}

kotlin {
    js(IR) {
        useCommonJs()
        browser {
            commonWebpackConfig(Action {
                cssSupport {
                    enabled.set(true)
                }
            })
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
//                implementation(kotlin("stdlib-js"))
//                implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${libs.versions.kotlin.wrappers.bom.get()}"))
//                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
//                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
//                implementation("org.jetbrains.kotlin-wrappers:kotlin-styled")
            }
        }
    }
}

dependencies {
    add("jsTestImplementation", kotlin("test"))
}
