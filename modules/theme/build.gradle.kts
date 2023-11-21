import com.cmtelematics.cmtreferenceapp.Libraries

android {
    namespace = "com.cmtelematics.cmtreferenceapp.theme"
}

dependencies {
    implementation(project(":common"))

    compileOnly(Libraries.AndroidX.annotation)

    api(Libraries.AndroidX.appcompat)
    api(Libraries.AndroidX.Ui.material)
    api(Libraries.AndroidX.Ui.constraintLayout)

    implementation(Libraries.Common.timber)

    // Compose
    api(Libraries.Compose.Ui.material)
    implementation(Libraries.Compose.Accompanist.insets)
    implementation(Libraries.Compose.Accompanist.navigationMaterial)
}
