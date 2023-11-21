// This file is needed because gradle searches for a settings.gradle or settings.gradle.kts file on every run.
// If this configuration weren't here, gradle would search the entire hierarchy every time which would slow down the build.
rootProject.buildFileName = "build.gradle.kts"
