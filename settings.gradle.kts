rootProject.name = "Viterbi"

// Enable Gradle configuration cache for better performance
org.gradle.configuration-cache = true

// Include our modules
include(":shared")
include(":androidApp")
include(":webApp")

// Enable Compose Multiplatform feature previews
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
} 