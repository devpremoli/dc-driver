package com.cmtelematics.cmtreferenceapp.common.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

fun <T : Route> NavGraphBuilder.composable(
    factory: Route.Factory<T>,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route = factory.path, arguments = arguments, deepLinks = deepLinks) { entry ->
        ScreenWithViewModel(navBackStackEntry = entry, routeFactory = factory) {
            content(entry)
        }
    }
}

fun <T : Route> NavGraphBuilder.dialog(
    factory: Route.Factory<T>,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    cancelable: Boolean = true,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    val dialogProperties = DialogProperties(
        dismissOnBackPress = cancelable,
        dismissOnClickOutside = cancelable
    )

    dialog(
        route = factory.path,
        arguments = arguments,
        deepLinks = deepLinks,
        dialogProperties = dialogProperties
    ) { entry ->
        ScreenWithViewModel(navBackStackEntry = entry, routeFactory = factory) {
            content(entry)
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun <T : Route> NavGraphBuilder.bottomSheet(
    factory: Route.Factory<T>,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable ColumnScope.(backstackEntry: NavBackStackEntry) -> Unit
) {
    bottomSheet(
        route = factory.path,
        arguments = arguments,
        deepLinks = deepLinks
    ) { entry ->
        ScreenWithViewModel(navBackStackEntry = entry, routeFactory = factory) {
            content(this, entry)
        }
    }
}

inline fun NavGraphBuilder.navigation(
    startDestination: Route.Factory<*>,
    route: Route.Factory<*>,
    builder: NavGraphBuilder.() -> Unit
) = navigation(startDestination.path, route.path, builder)
