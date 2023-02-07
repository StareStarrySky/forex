pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://www.dukascopy.com/client/jforexlib/publicrepo/")
    }
}

rootProject.name = "forex"
include(
    "forex-server",
    "forex-server:forex-base",
    "forex-server:forex-client",
    "forex-server:forex-JForex",
    "forex-server:forex-service",
    "forex-web"
)
