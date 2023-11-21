import com.cmtelematics.cmtreferenceapp.Libraries

android {
    namespace = "com.cmtelematics.cmtreferenceapp.common"
}

dependencies {
    api(project(":navigation"))

    implementation(Libraries.Cmt.driveWellSdk)

    api(Libraries.Kotlin.immutableCollection)
    api(Libraries.Kotlin.Coroutines.core)

    api(Libraries.AndroidX.Ktx.core)
    api(Libraries.AndroidX.Ktx.lifecycleScope)
    api(Libraries.AndroidX.Ktx.livedata)
    api(Libraries.AndroidX.Ktx.viewModel)

    implementation(Libraries.Kotlin.Serialization.json)
    implementation(Libraries.AndroidX.Ui.material)
    implementation(Libraries.AndroidX.Ui.constraintLayout)
    implementation(Libraries.AndroidX.datastore)

    implementation(Libraries.Common.timber)

    // Compose
    implementation(Libraries.Compose.Navigation.viewModel)
    implementation(Libraries.Compose.Accompanist.navigationMaterial)

    implementation(Libraries.Google.maps)

    // Navigation
    implementation(Libraries.Compose.Navigation.navigation)

    // Dagger
    implementation(Libraries.DI.Dagger.core)
    implementation(Libraries.DI.Hilt.core)
    kapt(Libraries.DI.Dagger.compiler)
    kapt(Libraries.DI.Hilt.compiler)
    compileOnly(Libraries.Common.inject)

    // Test
    api(Libraries.Test.KotlinX.coroutinesTest)
    api(Libraries.Test.mockk)
    api(Libraries.Test.truth)
    api(Libraries.Test.turbine)

    api(Libraries.Test.JUnit5.jupiterApi)
    api(Libraries.Test.JUnit5.jupiterParams)
    runtimeOnly(Libraries.Test.JUnit5.jupiterEngine)
}
