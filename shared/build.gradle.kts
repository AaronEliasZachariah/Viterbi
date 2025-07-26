plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.9.20"
}

kotlin {
    androidTarget()
    
    androidTarget()
    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibraryApi::class)
                implementation(compose.components.resources)
                
                // Coroutines for async programming
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                
                // Serialization for JSON handling
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                
                // Koin for dependency injection
                implementation("io.insert-koin:koin-core:3.5.0")
                implementation("io.insert-koin:koin-compose:1.1.0")
                
                // DateTime handling
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                // Android-specific dependencies will go here
            }
        }
        val jsMain by getting {
            dependencies {
                // Web-specific dependencies will go here
            }
        }
    }
}

android {
    namespace = "com.viterbi.shared"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 24
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
} 