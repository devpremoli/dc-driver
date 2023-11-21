package com.cmtelematics.cmtreferenceapp.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination
import androidx.navigation.compose.rememberNavController
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.common.model.PermissionState.Allowed
import com.cmtelematics.cmtreferenceapp.common.navigation.NavHost
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.common.util.parcelable
import com.cmtelematics.cmtreferenceapp.common.util.zipWithNext
import com.cmtelematics.cmtreferenceapp.common.viewmodel.LoaderState
import com.cmtelematics.cmtreferenceapp.common.viewmodel.LoaderViewModel
import com.cmtelematics.cmtreferenceapp.model.MainViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Constants
import com.cmtelematics.cmtreferenceapp.navigation.NavigationEvent
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.cmtelematics.cmtreferenceapp.navigation.RouteManager
import com.cmtelematics.cmtreferenceapp.navigation.delegate.NavigationDelegate
import com.cmtelematics.cmtreferenceapp.navigation.mainNavigation
import com.cmtelematics.cmtreferenceapp.navigation.route.MainGraphRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.SplashRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.welcome.WelcomeRoute
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.dialog.LoadingDialog
import com.cmtelematics.cmtreferenceapp.ui.component.SetupStatusBar
import com.cmtelematics.cmtreferenceapp.ui.component.SystemUIColorPalette
import com.cmtelematics.cmtreferenceapp.ui.component.TranslatedErrorDialog
import com.cmtelematics.cmtreferenceapp.ui.splash.SplashScreenInitializer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var routeManager: RouteManager

    @Inject
    lateinit var startupManager: StartupManager

    @Inject
    lateinit var errorService: ErrorService

    @Inject
    lateinit var splashScreenInitializer: SplashScreenInitializer

    @Inject
    lateinit var permissionManager: PermissionManager

    private val loaderViewModel: LoaderViewModel by viewModels()

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
            .setKeepOnScreenCondition(splashScreenInitializer)

        setContent {
            var systemUIColor by remember { mutableStateOf(SystemUIColorPalette.Default) }

            AppTheme {
                SetupStatusBar(systemUIColor)
                FullScreenLoader()

                Navigation { destination ->
                    handleStatusBarPaletteChange(destination) { palette ->
                        systemUIColor = palette
                    }
                }
                HandleErrors()
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, true)
    }

    @Composable
    private fun Navigation(onDestinationChanged: (destination: NavDestination) -> Unit) {
        val navController = rememberNavController()

        val keyboardController = LocalSoftwareKeyboardController.current

        NavHost(
            navController = navController,
            startDestination = SplashRoute,
            routeFactory = MainGraphRoute,
            modifier = Modifier.navigationBarsPadding()
        ) {
            mainNavigation()
        }

        LaunchedEffect(Unit) {
            val delegate = NavigationDelegate(navController)
            val route = navController.currentBackStackEntry?.run { destination.route }

            navController.currentBackStackEntryFlow
                .onEach {
                    onDestinationChanged(it.destination)
                }
                .launchIn(this)

            navController.currentBackStackEntryFlow
                .map { it.arguments?.parcelable(Constants.ARG_ROUTE) as Route? }
                .zipWithNext(::Pair)
                .onEach { (previousScreen, currentScreen) ->
                    mainViewModel.logScreenNavigation(previousScreen, currentScreen)
                }
                .launchIn(this)

            val isInitialised = startupManager.isAppInitialised().first()
            val hasDeniedPermission = permissionManager.state.map { permissionList ->
                permissionList.any { (_, permissionState) ->
                    permissionState != Allowed
                }
            }.first()

            // Override the restored route if the app isn't initialized or a permission trapdoor needs to be
            // shown.
            if (route != SplashRoute.path && !isInitialised) {
                delegate.navigate(NavigationEvent.ScreenNavigation(SplashRoute(shouldNavigateBack = true)))
            } else if (route != SplashRoute.path && hasDeniedPermission) {
                delegate.navigate(
                    NavigationEvent.ScreenNavigation(
                        route = SplashRoute(),
                        navigationOptions = NavigationOptions(popUpTo = MainGraphRoute, popUpToInclusive = true)
                    )
                )
            }

            for (navEvent in routeManager.dispatch()) {
                keyboardController?.hide()

                delegate.navigate(navEvent)
            }
        }
    }

    @Composable
    private fun FullScreenLoader() {
        val loaderState by loaderViewModel.fullScreenLoader.state.collectAsStateInLifecycle()

        if (loaderState == LoaderState.Show) {
            LoadingDialog()
        }
    }

    @Composable
    private fun HandleErrors() {
        var currentError by remember { mutableStateOf<Throwable?>(null) }
        val isErrorDialogVisible by derivedStateOf { currentError != null }

        if (isErrorDialogVisible) {
            TranslatedErrorDialog(
                onDismiss = { currentError = null },
                error = currentError ?: error("Can't happen because: isErrorDialogVisible == (currentError != null)")
            )
        }

        LaunchedEffect(Unit) {
            errorService.dispatch()
                .onEach { throwable ->
                    // note: cancellation can happen during navigation, it is expected and not an error
                    if (throwable is CancellationException) {
                        Timber.d(throwable)
                    } else {
                        Timber.e(throwable)
                        currentError = throwable
                    }
                }
                .launchIn(this)
        }
    }

    private fun handleStatusBarPaletteChange(
        destination: NavDestination,
        update: (SystemUIColorPalette) -> Unit
    ) = update(
        if (destination.route == SplashRoute.path || destination.route == WelcomeRoute.path) {
            SystemUIColorPalette.Splash
        } else {
            SystemUIColorPalette.Default
        }
    )
}
