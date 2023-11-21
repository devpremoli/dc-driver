package com.cmtelematics.cmtreferenceapp.plugin

import com.cmtelematics.cmtreferenceapp.Libraries
import com.cmtelematics.cmtreferenceapp.Versions
import configureLint
import configureVariants
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import java.io.File
import java.util.Locale

class QualityCheckConfigurationPlugin : Plugin<Project> {

    override fun apply(target: Project) = target.rootProject.run {
        val configPath = "${layout.projectDirectory}/quality/"

        allprojects {
            plugins.apply(DetektPlugin::class.java)

            extensions.configure<DetektExtension>() {
                toolVersion = Versions.detekt
                config = files("${configPath}detekt/detekt-config.yml")
            }

            tasks.withType<Detekt>().configureEach {
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    txt.required.set(false)
                    sarif.required.set(false)
                }

                classpath.setFrom(objects.fileCollection()) // turn off experimental type resolution for detekt
            }

            dependencies {
                add("detektPlugins", Libraries.Detekt.formatting)
            }

            plugins.withId("com.android.application") {
                configureAndroid(configPath)
            }

            plugins.withId("com.android.library") {
                configureAndroid(configPath)
            }
        }
    }
}

private fun Project.configureAndroid(configPath: String) {
    configureVariants { variant ->
        val variantName = variant.name.capitalize(Locale.getDefault())

        val checkTask = tasks.register("check$variantName") {
            description = "Runs code style checks on ${variant.name}"
            group = "Verification"

            dependsOn("lint$variantName")
            dependsOn("detekt$variantName")
        }

        tasks.named("check").configure {
            dependsOn(checkTask)
        }
    }

    configureLint {
        abortOnError = true
        xmlReport = true
        htmlReport = true

        lintConfig = File("${configPath}lint/lint.xml")
    }
}
