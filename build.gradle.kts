import com.cmtelematics.cmtreferenceapp.getArtifactoryProperties
import com.cmtelematics.cmtreferenceapp.getBaseUrl
import com.cmtelematics.cmtreferenceapp.getCompanyProperties
import com.cmtelematics.cmtreferenceapp.getGoogleMapsApiKey
import com.cmtelematics.cmtreferenceapp.getSdkApiKey
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.net.URL

requiredFiles("artifactory.properties", "company.properties")

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(com.cmtelematics.cmtreferenceapp.Libraries.Kotlin.Serialization.plugin)
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.20")
        classpath(com.cmtelematics.cmtreferenceapp.Libraries.Test.JUnit5.plugin)
    }
}

plugins {
    id("com.cmtelematics.cmtreferenceapp.config")
    // Plugin that applies static code analysis to the project
    id("com.cmtelematics.cmtreferenceapp.quality")
    // Plugin that determines which dependencies have updates
    id("com.github.ben-manes.versions") version "0.42.0"
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()

        // Add the Artifactory repositories that hold the SDK artifacts. Authenticate with usage of plain credentials.
        maven {
            val artifactory = getArtifactoryProperties()

            credentials {
                username = artifactory.getProperty("username")
                password = artifactory.getProperty("password")
            }

            url = project.uri(artifactory.getProperty("url"))
        }
    }
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        fun isNonStable(version: String): Boolean {
            val stableKeyword =
                listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
            val regex = "^[0-9,.v-]+(-r)?$".toRegex()
            val isStable = stableKeyword || regex.matches(version)
            return isStable.not()
        }

        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

val companyProperties = getCompanyProperties()

with(companyProperties.getBaseUrl()) {
    check(!this.isNullOrBlank()) { "Server URL is missing" }
    URL(this)
}
companyProperties.getSdkApiKey()?.takeIf { it.isNotBlank() } ?: error("DriveWell SDK API key is missing")
companyProperties.getGoogleMapsApiKey()?.takeIf { it.isNotBlank() } ?: error("Google maps API key is missing")
