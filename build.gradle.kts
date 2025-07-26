plugins {
    // Apply the Kotlin Multiplatform plugin
    kotlin("multiplatform") version "1.9.20" apply false
    // Apply the Android plugin
    id("com.android.application") version "8.1.4" apply false
    id("com.android.library") version "8.1.4" apply false
    // Apply the Compose Multiplatform plugin
    id("org.jetbrains.compose") version "1.5.10" apply false
}

// Configure all projects
allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
} 