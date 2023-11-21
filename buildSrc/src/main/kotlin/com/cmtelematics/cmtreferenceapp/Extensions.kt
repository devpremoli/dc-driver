@file:Suppress("DEPRECATION")

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.Lint
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.cmtelematics.cmtreferenceapp.assertFilesExist
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.DomainObjectSet
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

// debug build type extensions
val <T> NamedDomainObjectCollection<T>.debug: T
    get() = getByName(TYPE_DEBUG)

fun <T> NamedDomainObjectCollection<T>.debug(configureAction: T.() -> Unit): T =
    getByName(TYPE_DEBUG, configureAction)

// release build type extensions
val <T> NamedDomainObjectCollection<T>.release: T
    get() = getByName(TYPE_RELEASE)

fun <T> NamedDomainObjectCollection<T>.release(configureAction: T.() -> Unit): T =
    getByName(TYPE_RELEASE, configureAction)

// other
internal val Project.android
    get() = extensions.getByType<BaseExtension>()

internal fun Project.android(configure: BaseExtension.() -> Unit) =
    extensions.configure(configure)

internal fun Project.detekt(configure: DetektExtension.() -> Unit) =
    extensions.configure(configure)

val Project.variants: DomainObjectSet<out BaseVariant>
    get() = if (plugins.hasPlugin("android-library")) {
        extensions.getByType<LibraryExtension>().libraryVariants
    } else {
        extensions.getByType<AppExtension>().applicationVariants
    }

val BaseExtension.variants: DomainObjectSet<out BaseVariant>?
    get() = when (this) {
        is AppExtension -> applicationVariants
        is LibraryExtension -> libraryVariants
        else -> null
    }

fun Project.configureVariants(configure: (BaseVariant) -> Unit) =
    extensions.configure<BaseExtension> {
        variants?.all { configure(this) }
    }

fun Project.configureLint(action: Lint.() -> Unit) {
    extensions.configure<BaseExtension> {
        if (this is CommonExtension<*, *, *, *>) {
            lint { action() }
        }
    }
}

// validations
fun Project.requiredFiles(vararg paths: String) {
    gradle.addProjectEvaluationListener(object : ProjectEvaluationListener {

        override fun beforeEvaluate(project: Project) {
            assertFilesExist(paths.toList())
        }

        override fun afterEvaluate(project: Project, state: ProjectState) {}
    })
}

fun String.getDependencyVersion(): String = substringAfterLast(':')

fun String.getDependencyVersionWithoutCommitHash(): String = getDependencyVersion().let { dependencyVersion ->
    dependencyVersion.substringBefore(
        delimiter = '-',
        missingDelimiterValue = dependencyVersion
    )
}
