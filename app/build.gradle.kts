@file:Suppress("UnstableApiUsage")

import com.cmtelematics.cmtreferenceapp.Libraries
import com.cmtelematics.cmtreferenceapp.getBaseUrl
import com.cmtelematics.cmtreferenceapp.getCompanyProperties
import com.cmtelematics.cmtreferenceapp.getGoogleMapsApiKey
import com.cmtelematics.cmtreferenceapp.getSdkApiKey

plugins {
    id("dagger.hilt.android.plugin")
    id("com.cmtelematics.cmtreferenceapp.conditional-firebase")
}

android {
    namespace = "com.cmtelematics.cmtreferenceapp"

    defaultConfig {

        applicationId = "com.cmtelematics.cmtreferenceapp"

        versionCode = 1
        versionName = Libraries.Cmt.driveWellSdk.getDependencyVersionWithoutCommitHash()

        val companyProperties = getCompanyProperties()

        buildConfigField("String", "API_ENDPOINT", "\"${companyProperties.getBaseUrl()}\"")
        buildConfigField("String", "API_KEY", "\"${companyProperties.getSdkApiKey()}\"")
        buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"${companyProperties.getGoogleMapsApiKey()}\"")
        buildConfigField("String", "SDK_VERSION", "\"${Libraries.Cmt.driveWellSdk.getDependencyVersion()}\"")

        addManifestPlaceholders(
            mapOf(
                "debugGoogleMapsApiKey" to companyProperties.getProperty("googleMapsApiKey")
            )
        )
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
    }

    signingConfigs {
        // TODO: replace with release keystore
        register(TYPE_RELEASE) {
            storeFile = file("keystore/debug.keystore")

            keyAlias = "androiddebugkey"
            storePassword = "android"
            keyPassword = "android"
        }

        getByName(TYPE_DEBUG) {
            storeFile = file("keystore/debug.keystore")

            keyAlias = "androiddebugkey"
            storePassword = "android"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName(TYPE_RELEASE) {
            // Apply the signing config for release build type
            signingConfig = signingConfigs.getByName(TYPE_RELEASE)

            // Specifies whether to enable code shrinking for this build type
            isMinifyEnabled = false
            // Specifies whether to enable resource shrinking, which is performed by
            // the Android Gradle plugin
            isShrinkResources = false

            // Includes ProGuard rules files that are packaged with the Android Gradle plugin,
            // and an additional one that is project specific.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName(TYPE_DEBUG) {
            // Apply the signing config for debug build type
            signingConfig = signingConfigs.getByName(TYPE_DEBUG)
        }

        // Add to resolve warnings about duplicate files
        packagingOptions {
            resources.excludes.add("META-INF/NOTICE.txt")
            resources.excludes.add("META-INF/LICENSE.txt")
        }
    }

    buildFeatures {
        // Flag to enable/disable generation of the BuildConfig class
        buildConfig = true
    }
}

dependencies {
    implementation(project(":navigation"))
    implementation(project(":theme"))
    implementation(project(":common"))
    implementation(project(":wrappers"))

    implementation(project(":features:welcome"))
    implementation(project(":features:authentication"))
    implementation(project(":features:dashboard"))
    implementation(project(":features:tags"))
    implementation(project(":features:permission"))
    implementation(project(":features:settings"))
    implementation(project(":features:trips"))
    implementation(project(":features:crash"))

    implementation(Libraries.AndroidX.appcompat)
    implementation(Libraries.AndroidX.datastore)
    implementation(Libraries.AndroidX.Ktx.core)
    implementation(Libraries.AndroidX.Ui.activity)
    implementation(Libraries.AndroidX.Ui.splashScreen)
    implementation(Libraries.DI.Hilt.hiltWork)
    kapt(Libraries.DI.Hilt.androidXCompiler)

    implementation(Libraries.Compose.activity)
    implementation(Libraries.Compose.viewModel)
    implementation(Libraries.Compose.viewBinding)

    implementation(Libraries.Compose.Navigation.navigation)
    implementation(Libraries.Compose.Navigation.viewModel)

    implementation(Libraries.Compose.Accompanist.insets)
    implementation(Libraries.Compose.Accompanist.systemUiController)
    implementation(Libraries.Compose.Accompanist.navigationMaterial)

    implementation(Libraries.DI.Dagger.core)
    implementation(Libraries.DI.Hilt.core)
    kapt(Libraries.DI.Dagger.compiler)
    kapt(Libraries.DI.Hilt.compiler)
    compileOnly(Libraries.Common.inject)

    implementation(Libraries.Networking.OkHttp.core)
    implementation(Libraries.Networking.OkHttp.logger)

    implementation(Libraries.Common.timber)

    implementation(platform(Libraries.Google.Firebase.bom))
    implementation(Libraries.Google.Firebase.crashlyticsKtx)
    implementation(Libraries.Google.Firebase.analyticsKtx)
    implementation(Libraries.Google.Firebase.cloudMessaging)

    implementation(Libraries.Google.exoplayer)
}
