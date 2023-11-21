package com.cmtelematics.cmtreferenceapp.plugin

import com.cmtelematics.cmtreferenceapp.Libraries
import com.cmtelematics.cmtreferenceapp.Versions
import android
import com.android.build.api.dsl.ApplicationBuildFeatures
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryBuildFeatures
import com.android.build.gradle.BaseExtension
import configureVariants
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ProjectConfigurationPlugin : Plugin<Project> {

    @Suppress("LongMethod")
    override fun apply(target: Project) {
        target.subprojects {
            tasks.withType<JavaCompile> {
                options.compilerArgs.addAll(listOf("-Xmaxerrs", Int.MAX_VALUE.toString()))
            }

            tasks.withType<KotlinCompile> {
                kotlinOptions {
                    freeCompilerArgs = listOf(
                        "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                        "-Xopt-in=kotlin.RequiresOptIn",
                        "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-Xopt-in=kotlinx.coroutines.FlowPreview",
                        "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
                        "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
                        "-Xopt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi",
                        "-Xopt-in=kotlin.ExperimentalStdlibApi"
                    )
                }
            }
        }

        target.configure(target.subprojects.filter { it.file("src/main/AndroidManifest.xml").exists() }) {
            if (name == "app") {
                plugins.apply("com.android.application")
            } else {
                plugins.apply("com.android.library")
            }

            plugins.apply("org.jetbrains.kotlin.android")
            plugins.apply("org.jetbrains.kotlin.kapt")
            plugins.apply("de.mannodermaus.android-junit5")

            android {
                compileSdkVersion(Versions.compileSdk)

                defaultConfig {
                    minSdk = Versions.minsdk
                    targetSdk = Versions.targetsdk

                    vectorDrawables.setGeneratedDensities(emptyList())

                    if (project.file("proguard-rules.pro").exists()) {
                        consumerProguardFiles("proguard-rules.pro")
                    }
                }

                compileOptions {
                    // Flag to enable support for the new language APIs
                    isCoreLibraryDesugaringEnabled = true
                }

                flavorDimensions("default")

                productFlavors.configureEach {
                    dimension = "default"

                    sourceSets.getByName(name) {
                        java.srcDir("src/$name/kotlin")
                    }
                }

                buildTypes.configureEach {
                    sourceSets.getByName(name) {
                        java.srcDir("src/$name/kotlin")
                    }
                }

                configureVariants { variant ->
                    sourceSets.getByName(variant.name) {
                        java.srcDir("src/${variant.name}/kotlin")
                    }
                }

                sourceSets {
                    getByName("main").java.srcDir("src/main/kotlin")
                    getByName("test").java.srcDir("src/test/kotlin")
                    getByName("androidTest").java.srcDir("src/androidTest/kotlin")
                }
            }

            dependencies {
                add("coreLibraryDesugaring", Libraries.Android.jdk8)
                add("testImplementation", Libraries.Test.JUnit5.jupiterApi)
                add("testRuntimeOnly", Libraries.Test.JUnit5.jupiterEngine)
                add("testImplementation", Libraries.Test.JUnit5.jupiterParams)
            }

            extensions.configure<KaptExtension> {
                correctErrorTypes = true

                javacOptions {
                    option("-Xmaxerrs", Int.MAX_VALUE)
                }
            }
        }

        target.configure(target.subprojects.filter { it.hasUi() }) {
            android {
                if (name == "app") {
                    applicationBuildFeatures {
                        buildConfig = true
                        compose = true
                    }
                } else {
                    libraryBuildFeatures {
                        compose = true
                    }
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = Versions.composeCompiler
                }
            }

            dependencies {
                add("implementation", Libraries.Compose.ui)
                add("debugImplementation", Libraries.Compose.tooling)
                add("implementation", Libraries.Compose.preview)
                add("implementation", Libraries.Compose.foundation)
            }

            if (parent?.name == "features") {
                plugins.apply("dagger.hilt.android.plugin")
            }
        }
    }
}

private fun Project.hasUi() = parent?.name == "features" || name == "app" || name == "theme" || name.contains("common")

private fun BaseExtension.applicationBuildFeatures(action: ApplicationBuildFeatures.() -> Unit) {
    @Suppress("UNCHECKED_CAST")
    (this as CommonExtension<ApplicationBuildFeatures, *, *, *>).buildFeatures(action)
}

private fun BaseExtension.libraryBuildFeatures(action: LibraryBuildFeatures.() -> Unit) {
    @Suppress("UNCHECKED_CAST")
    (this as CommonExtension<LibraryBuildFeatures, *, *, *>).buildFeatures(action)
}
