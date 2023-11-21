package com.cmtelematics.cmtreferenceapp.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.getAll
import androidx.navigation.NavBackStackEntry
import com.cmtelematics.cmtreferenceapp.common.ui.util.requireActivity
import com.cmtelematics.cmtreferenceapp.common.util.parcelable
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseViewModel
import com.cmtelematics.cmtreferenceapp.common.viewmodel.Loader
import com.cmtelematics.cmtreferenceapp.common.viewmodel.LoaderViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Constants.ARG_RESULT
import com.cmtelematics.cmtreferenceapp.navigation.Constants.ARG_ROUTE
import com.cmtelematics.cmtreferenceapp.navigation.NavigationResult
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ScreenWithViewModel(
    navBackStackEntry: NavBackStackEntry,
    routeFactory: Route.Factory<*>,
    content: @Composable () -> Unit
) {
    content()

    val activity = LocalContext.current.requireActivity()
    val loaderViewModel: LoaderViewModel = hiltViewModel(viewModelStoreOwner = activity)

    val viewModel = remember {
        navBackStackEntry.viewModelStore.getAll()
            .filterIsInstance<BaseScreenViewModel>()
            .firstOrNull()
    }

    LaunchedEffect(Unit) {
        val route = navBackStackEntry.arguments?.parcelable<Route>(ARG_ROUTE)

        if (viewModel != null) {
            viewModel.setCurrentRoute(route ?: routeFactory.create(navBackStackEntry.arguments))

            handleNavigationResult(navBackStackEntry, viewModel)

            handleLoaderState(navBackStackEntry, loaderViewModel)
        }
    }
}

private fun CoroutineScope.handleNavigationResult(
    navBackStackEntry: NavBackStackEntry,
    viewModel: BaseScreenViewModel
) {
    val navigationResultLiveData = navBackStackEntry.savedStateHandle
        .getLiveData<NavigationResult>(ARG_RESULT)

    navigationResultLiveData.asFlow()
        .filterNotNull()
        .onEach { navigationResult ->
            viewModel.setNavigationResult(navigationResult)
            navigationResultLiveData.value = null
        }
        .launchIn(this)
}

private fun CoroutineScope.handleLoaderState(
    navBackStackEntry: NavBackStackEntry,
    loaderViewModel: LoaderViewModel
) {
    navBackStackEntry.viewModelStore.getAll()
        .filterIsInstance<BaseViewModel>()
        .onEach { viewModel ->
            handleLoaderChanges(viewModel.fullScreenLoading, loaderViewModel.fullScreenLoader)
            handleLoaderChanges(viewModel.inlineLoading, loaderViewModel.inlineLoader)
        }
}

private fun CoroutineScope.handleLoaderChanges(
    loaderChanges: Flow<Boolean>,
    loader: Loader
) {
    loaderChanges
        .onEach { showLoader ->
            if (showLoader) {
                loader.show()
            } else {
                loader.hide()
            }
        }
        .launchIn(this)
}
