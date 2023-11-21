import com.cmtelematics.cmtreferenceapp.Libraries

android {
    namespace = "com.cmtelematics.cmtreferenceapp.settings"
}

dependencies {
    implementation(project(":navigation"))
    implementation(project(":theme"))
    implementation(project(":common"))
    implementation(project(":wrappers"))

    // Compose
    implementation(Libraries.Compose.Navigation.viewModel)

    implementation(Libraries.Common.timber)

    // Dagger
    implementation(Libraries.DI.Dagger.core)
    implementation(Libraries.DI.Hilt.core)
    kapt(Libraries.DI.Dagger.compiler)
    kapt(Libraries.DI.Hilt.compiler)
    compileOnly(Libraries.Common.inject)
}
