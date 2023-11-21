rootProject.name = "ReferenceApp"
rootProject.buildFileName = "build.gradle.kts"

// base modules
include(":app", ":navigation", ":common", ":theme", ":wrappers")

project(":navigation").projectDir = file("./modules/navigation")
project(":common").projectDir = file("./modules/common")
project(":theme").projectDir = file("./modules/theme")
project(":wrappers").projectDir = file("./modules/wrappers")

// feature modules
include(":features")
project(":features").projectDir = file("./modules/features")

include(":features:welcome")
project(":features:welcome").projectDir = file("./modules/features/welcome")

include(":features:authentication")
project(":features:authentication").projectDir = file("./modules/features/authentication")

include(":features:dashboard")
project(":features:dashboard").projectDir = file("./modules/features/dashboard")

include(":features:tags")
project(":features:tags").projectDir = file("./modules/features/tags")

include(":features:permission")
project(":features:permission").projectDir = file("./modules/features/permission")

include(":features:settings")
project(":features:settings").projectDir = file("./modules/features/settings")

include(":features:trips")
project(":features:trips").projectDir = file("./modules/features/trips")

include(":features:crash")
project(":features:crash").projectDir = file("./modules/features/crash")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.google.gms.google-services") {
                useModule("com.google.gms:google-services:4.3.10")
            }
        }
    }
}
