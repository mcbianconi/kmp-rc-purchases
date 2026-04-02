@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
//    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    // https://kotlinlang.org/docs/multiplatform/multiplatform-hierarchy.html#apply-the-default-hierarchy-template
    // Required alongside manual dependsOn() calls (webMain) to avoid suppressing the default hierarchy.
    applyDefaultHierarchyTemplate()

    jvmToolchain(21)

    jvm()
    js().browser()
    wasmJs().browser()

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        // Enable ExperimentalForeignApi for all Apple platforms
        named {
            it.lowercase().let { n -> n.startsWith("ios") || n.startsWith("macos") }
        }.configureEach {
            languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "purchasesKit"
    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64(),
        macosArm64()
    ).forEach {
        it.binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                // Compose
                implementation(compose.runtime)
                implementation(compose.ui)

                // Koin
                implementation(libs.koin.core)

                // RevenueCat
                implementation(libs.purchases.core)
                implementation(libs.purchases.ui)
                implementation(libs.purchases.either)
                implementation(libs.purchases.result)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.revenuecat.android)
                implementation(libs.revenuecat.android.ui)
                implementation(libs.koin.android)
            }
        }

    }
}


android {
    namespace = "com.bearminds.purchases"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Exclude RevenueCat from Unsupported platforms configurations
// NOTE: RevenueCat KMP only supports iOS and Android - macOS is excluded
afterEvaluate {
    configurations.matching {
        it.name.contains("jvm", ignoreCase = true) ||
                it.name.contains("macos", ignoreCase = true) ||
                it.name.contains("js", ignoreCase = true)
    }.configureEach {
        exclude(group = "com.revenuecat.purchases", module = "purchases-kmp-core")
        exclude(group = "com.revenuecat.purchases", module = "purchases-kmp-either")
        exclude(group = "com.revenuecat.purchases", module = "purchases-kmp-result")
        exclude(group = "com.revenuecat.purchases", module = "purchases-kmp-ui")
    }

    // Exclude Amazon Appstore SDK globally - we only use Google Play
    configurations.configureEach {
        exclude(group = "com.revenuecat.purchases", module = "purchases-store-amazon")
        exclude(group = "com.amazon.device", module = "amazon-appstore-sdk")
    }
}
