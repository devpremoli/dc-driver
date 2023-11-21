import com.cmtelematics.cmtreferenceapp.Libraries

plugins {
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.cmtelematics.cmtreferenceapp.wrappers"
}

dependencies {
    implementation(project(":common"))

    implementation(Libraries.Cmt.driveWellSdk)
    implementation(Libraries.Cmt.sensorFlow)
    implementation(Libraries.Compose.Navigation.common)

    compileOnly(Libraries.AndroidX.annotation)
    implementation(Libraries.AndroidX.datastore)
    implementation(Libraries.AndroidX.Ktx.workRuntime)

    implementation(Libraries.Common.timber)

    // Serialization
    implementation(Libraries.Kotlin.Serialization.json)

    // Dagger
    compileOnly(Libraries.Common.inject)
    implementation(Libraries.DI.Dagger.core)
    implementation(Libraries.DI.Hilt.core)
    implementation(Libraries.DI.Hilt.hiltWork)
    kapt(Libraries.DI.Dagger.compiler)
    kapt(Libraries.DI.Hilt.compiler)
    kapt(Libraries.DI.Hilt.androidXCompiler)

    implementation(platform(Libraries.Google.Firebase.bom))
    implementation(Libraries.Google.Firebase.cloudMessaging)

    implementation(Libraries.Kotlin.Coroutines.rx2)
}
