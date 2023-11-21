package com.cmtelematics.cmtreferenceapp

import org.gradle.api.Project
import java.util.Properties

fun Project.getArtifactoryProperties(): Properties = getProperties("artifactory.properties")

fun Project.getCompanyProperties(): Properties = getProperties("company.properties")

fun Project.assertFilesExist(fileNames: List<String>) {
    val missingFiles = StringBuilder()
    fileNames.forEach { fileName ->
        val file = rootProject.file(fileName)
        if (file.exists().not()) {
            missingFiles.append(fileName)
                .also { it.append("\n") }
        }
    }
    if (missingFiles.isNotBlank()) {
        throw IllegalArgumentException("Please check if the following files exist and follow the README carefully:\n$missingFiles")
    }
}

private fun Project.getProperties(fileName: String): Properties {
    val propertiesFile = rootProject.file(fileName)

    require(propertiesFile.exists()) { "'$fileName' does not exist." }

    return Properties().apply {
        load(propertiesFile.inputStream())
    }
}

fun Properties.getBaseUrl(): String? = getProperty("baseUrl")

fun Properties.getSdkApiKey(): String? = getProperty("apiKey")

fun Properties.getGoogleMapsApiKey(): String? = getProperty("googleMapsApiKey")
