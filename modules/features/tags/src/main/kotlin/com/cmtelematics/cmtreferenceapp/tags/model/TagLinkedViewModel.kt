package com.cmtelematics.cmtreferenceapp.tags.model

import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.dashboard.DashboardRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the screen shown after successfully linking a tag.
 *
 * @property navigator Framework independent navigator.
 * @property errorService Framework independent error sink.
 */
@HiltViewModel
internal class TagLinkedViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService
) : BaseScreenViewModel(navigator, errorService) {
    fun closeScreen() = launchWithErrorHandling {
        navigator.navigateBackTo(routeFactory = DashboardRoute, inclusive = false)
    }
}
