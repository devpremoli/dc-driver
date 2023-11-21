package com.cmtelematics.cmtreferenceapp.welcome.model

import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.authentication.ExistingUserEmailRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.authentication.RegisterEmailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService
) : BaseScreenViewModel(navigator, errorService) {

    fun navigateToRegisterEmailScreen() = launchWithErrorHandling {
        navigator.navigateTo(RegisterEmailRoute())
    }

    fun navigateToExistingUserEmailScreen() = launchWithErrorHandling {
        navigator.navigateTo(ExistingUserEmailRoute())
    }
}
