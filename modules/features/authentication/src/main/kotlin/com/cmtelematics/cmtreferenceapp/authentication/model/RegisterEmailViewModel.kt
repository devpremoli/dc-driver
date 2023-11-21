package com.cmtelematics.cmtreferenceapp.authentication.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.PermanentDataStore
import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.welcome.WelcomeRoute
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * This ViewModel handles the email input and verification for the the registration flow.
 *
 * @property navigator Framework independent navigator.
 * @property errorService Framework independent error sink.
 * @property authenticationManager Provides authentication-related function.
 * @property dataStore Permanent DataStore of application.
 */
@HiltViewModel
internal class RegisterEmailViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val authenticationManager: AuthenticationManager,
    private val routeFactory: MainRouteFactory,
    @PermanentDataStore dataStore: DataStore<Preferences>,
    dispatcherProvider: DispatcherProvider
) : BaseEmailAuthenticationViewModel(navigator, errorService, dataStore, dispatcherProvider) {
    val tagUser = MutableStateFlow(false)

    override suspend fun submitEmail() {
        authenticationManager.register(email = email.value, tagUser = tagUser.value)

        navigator.navigateTo(
            route = routeFactory.createOrDefault(),
            navigationOptions = NavigationOptions(
                popUpTo = WelcomeRoute,
                popUpToInclusive = true
            )
        )
    }
}
