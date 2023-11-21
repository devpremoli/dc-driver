import com.cmtelematics.cmtreferenceapp.Libraries

plugins {
    kotlin("plugin.parcelize")
}

android {
    namespace = "com.cmtelematics.cmtreferenceapp.navigation"
}

dependencies {
    compileOnly(Libraries.AndroidX.annotation)

    api(Libraries.Kotlin.Coroutines.core)

    implementation(Libraries.Common.timber)
    implementation(Libraries.Compose.Navigation.navigation)
}
