import com.cmtelematics.cmtreferenceapp.Libraries

android {
    namespace = "com.cmtelematics.cmtreferenceapp.trips"
}

dependencies {
    implementation(project(":navigation"))
    implementation(project(":theme"))
    implementation(project(":common"))
    implementation(project(":wrappers"))

    implementation(Libraries.Cmt.driveWellSdk)

    implementation(Libraries.AndroidX.datastore)

    // Compose
    implementation(Libraries.Compose.Navigation.viewModel)
    implementation(Libraries.Compose.Accompanist.swipeRefreshLayout)
    implementation(Libraries.Compose.Ui.mapView)

    implementation(Libraries.Google.maps)
    implementation(Libraries.Google.mapsKtx)
    implementation(Libraries.Google.mapUtils)

    implementation(Libraries.Common.timber)
    implementation(Libraries.Common.coilCompose)

    // Dagger
    implementation(Libraries.DI.Dagger.core)
    implementation(Libraries.DI.Hilt.core)
    kapt(Libraries.DI.Dagger.compiler)
    kapt(Libraries.DI.Hilt.compiler)
    compileOnly(Libraries.Common.inject)
}
