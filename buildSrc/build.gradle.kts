plugins {
    `kotlin-dsl`
}

group = "com.cmtelematics.cmtreferenceapp"

gradlePlugin {
    plugins {
        create("config") {
            id = "com.cmtelematics.cmtreferenceapp.config"
            implementationClass = "com.cmtelematics.cmtreferenceapp.plugin.ProjectConfigurationPlugin"
        }

        create("quality") {
            id = "com.cmtelematics.cmtreferenceapp.quality"
            implementationClass = "com.cmtelematics.cmtreferenceapp.plugin.QualityCheckConfigurationPlugin"
        }

        create("conditional-firebase") {
            id = "com.cmtelematics.cmtreferenceapp.conditional-firebase"
            implementationClass = "com.cmtelematics.cmtreferenceapp.plugin.ConditionalFirebasePlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {

    // Android plugin dependencies
    implementation("com.android.tools.build:gradle:7.4.1")

    // Kotlin plugin dependencies
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")

    // Hilt dependencies
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.43.2")

    // Quality plugins
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.21.0")

    // Kotlin documentation generation
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.0")

    // Json
    implementation("org.json:json:20210307")
}
