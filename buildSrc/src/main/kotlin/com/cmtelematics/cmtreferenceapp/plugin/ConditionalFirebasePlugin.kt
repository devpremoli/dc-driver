package com.cmtelematics.cmtreferenceapp.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 * Only apply the GS and Firebase plugins if google-services.json is present in the module's directory. Using this
 * plugin devs don't need to have the firebase config present on their computers.
 */
class ConditionalFirebasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (File(target.projectDir, "google-services.json").exists()) {
            target.plugins.apply("com.google.gms.google-services")
            target.plugins.apply("com.google.firebase.crashlytics")
        }
    }
}
