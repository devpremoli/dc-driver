package com.cmtelematics.cmtreferenceapp

object Versions {
    internal const val detekt = "1.22.0"

    const val minsdk = 24
    const val targetsdk = 33
    const val compileSdk = 33

    const val kotlin = "1.8.10"
    const val coroutines = "1.6.4"

    const val lifecycle = "2.5.1"
    const val activity = "1.5.1"

    const val compose = "1.2.1"
    const val composeCompiler = "1.3.1"
    const val accompanist = "0.25.1"

    const val dagger = "2.43.2"
    const val hiltWork = "1.0.0"

    const val okhttp = "4.10.0"

    const val bindingCollectionAdapter = "4.0.0"

    const val testCore = "1.4.0"
    const val testRunner = "1.4.1"
    const val jUnit5Plugin = "1.8.2.1"
    const val jUnit5 = "5.8.2"
}

object Libraries {

    object Cmt {
        const val driveWellSdk = "com.cmtelematics.sdk:model:12.2.2-rc18311-0c0399a58"
        const val sensorFlow = "com.cmtelematics.sdk:SensorFlow:0.1.2.1"
    }

    object Android {
        const val jdk8 = "com.android.tools:desugar_jdk_libs:2.0.0"
    }

    object Kotlin {

        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
        const val immutableCollection = "org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5"

        object Coroutines {
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
            const val rx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:${Versions.coroutines}"
        }

        object Serialization {
            const val plugin = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
            const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"
        }
    }

    object AndroidX {
        const val annotation = "androidx.annotation:annotation:1.2.0"
        const val appcompat = "androidx.appcompat:appcompat:1.5.1"
        const val datastore = "androidx.datastore:datastore-preferences:1.0.0"

        object Ktx {
            const val core = "androidx.core:core-ktx:1.9.0"
            const val lifecycleScope =
                "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
            const val workRuntime = "androidx.work:work-runtime-ktx:2.7.1"
        }

        object Ui {
            const val material = "com.google.android.material:material:1.6.1"
            const val activity = "androidx.activity:activity-ktx:1.5.1"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
            const val splashScreen = "androidx.core:core-splashscreen:1.0.0"
        }
    }

    object Compose {
        const val runtime = "androidx.compose.runtime:runtime:${Versions.compose}"
        const val foundation = "androidx.compose.foundation:foundation:${Versions.compose}"
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val preview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val viewBinding = "androidx.compose.ui:ui-viewbinding:${Versions.compose}"

        const val activity = "androidx.activity:activity-compose:${Versions.activity}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"

        object Ui {
            const val material = "androidx.compose.material:material:${Versions.compose}"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.1"
            const val mapView = "com.google.maps.android:maps-compose:2.5.3"
        }

        object Navigation {
            const val navigation = "androidx.navigation:navigation-compose:2.5.2"
            const val viewModel = "androidx.hilt:hilt-navigation-compose:1.0.0"
            const val common = "androidx.navigation:navigation-common-ktx:2.5.1"
        }

        object Accompanist {
            const val insets = "com.google.accompanist:accompanist-insets:${Versions.accompanist}"
            const val systemUiController =
                "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}"
            const val navigationMaterial =
                "com.google.accompanist:accompanist-navigation-material:${Versions.accompanist}"
            const val swipeRefreshLayout =
                "com.google.accompanist:accompanist-swiperefresh:${Versions.accompanist}"
        }
    }

    object Google {
        const val mapsKtx = "com.google.maps.android:maps-ktx:3.4.0"
        const val mapUtils = "com.google.maps.android:android-maps-utils:2.3.0"
        const val maps = "com.google.android.gms:play-services-maps:18.0.2"
        const val location = "com.google.android.gms:play-services-location:20.0.0"
        const val exoplayer = "com.google.android.exoplayer:exoplayer:2.18.2"

        object Firebase {
            const val bom = "com.google.firebase:firebase-bom:30.5.0"
            const val crashlyticsKtx = "com.google.firebase:firebase-crashlytics-ktx"
            const val analyticsKtx = "com.google.firebase:firebase-analytics-ktx"
            const val cloudMessaging = "com.google.firebase:firebase-messaging-ktx"
        }
    }

    object DI {
        object Dagger {
            const val core = "com.google.dagger:dagger:${Versions.dagger}"
            const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
        }

        object Hilt {
            const val core = "com.google.dagger:hilt-android:${Versions.dagger}"
            const val compiler = "com.google.dagger:hilt-compiler:${Versions.dagger}"
            const val hiltWork = "androidx.hilt:hilt-work:${Versions.hiltWork}"
            const val androidXCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltWork}"
        }
    }

    object Networking {

        object OkHttp {
            const val core = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
            const val logger = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
        }
    }

    object Common {
        const val timber = "com.jakewharton.timber:timber:5.0.1"
        const val inject = "javax.inject:javax.inject:1"
        const val coilCompose = "io.coil-kt:coil-compose:1.4.0"
    }

    object Detekt {
        const val formatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"
    }

    object Test {
        const val mockk = "io.mockk:mockk:1.13.3"
        const val truth = "com.google.truth:truth:1.1.3"
        const val turbine = "app.cash.turbine:turbine:0.9.0"

        object KotlinX {
            const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4"
        }

        object JUnit5 {
            const val plugin = "de.mannodermaus.gradle.plugins:android-junit5:${Versions.jUnit5Plugin}"
            const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.jUnit5}"
            const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.jUnit5}"
            const val jupiterParams = "org.junit.jupiter:junit-jupiter-params:${Versions.jUnit5}"
        }
    }
}
