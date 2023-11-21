package com.cmtelematics.cmtreferenceapp.authentication.model

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.authentication.VerifyCodeRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.welcome.WelcomeRoute
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * This ViewModel handles the otp input and verifies the given code.
 *
 * @property navigator Framework independent navigator.
 * @property errorService Framework independent error sink.
 * @property authenticationManager Provides authentication-related function.
 */
@HiltViewModel
internal class VerifyCodeViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val authenticationManager: AuthenticationManager,
    private val routeFactory: MainRouteFactory
) : BaseScreenViewModel(navigator, errorService) {

    val code = MutableStateFlow("")
    val submitEnabled = code.map { it.isOtpCodeValid() }
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = false)

    private val email
        get() = currentRoute<VerifyCodeRoute>().value?.email ?: error("Email must not be null")

    fun submit() = launchWithErrorHandling {
        trackProgress {
            authenticationManager.loginWithOTP(email, code.value)

            navigator.navigateTo(
                route = routeFactory.createOrDefault(),
                navigationOptions = NavigationOptions(
                    popUpTo = WelcomeRoute,
                    popUpToInclusive = true
                )
            )
        }
    }

    fun resendCode() = launchWithErrorHandling {
        trackProgress {
            authenticationManager.verify(email = email)
        }
    }

    private fun String.isOtpCodeValid(): Boolean = this.length == OTP_CODE_LENGTH && this.isDigitsOnly()

    companion object {
        private const val OTP_CODE_LENGTH = 4
    }
}
