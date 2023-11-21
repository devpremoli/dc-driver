package com.cmtelematics.cmtreferenceapp.authentication.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.PermanentDataStore
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.authentication.VerifyCodeRoute
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This ViewModel handles the email input and requesting OTP for code login.
 *
 * @property navigator Framework independent navigator.
 * @property errorService Framework independent error sink.
 * @property authenticationManager Provides authentication-related function.
 * @property dataStore Permanent DataStore of application.
 */
@HiltViewModel
internal class ExistingUserEmailViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val authenticationManager: AuthenticationManager,
    @PermanentDataStore dataStore: DataStore<Preferences>,
    dispatcherProvider: DispatcherProvider
) : BaseEmailAuthenticationViewModel(navigator, errorService, dataStore, dispatcherProvider) {

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            dataStore.data.map { it[latestEmailPrefsKey] }.first()?.let { email.value = it }
        }
    }

    override suspend fun submitEmail() {
        authenticationManager.verify(email = email.value)
        navigator.navigateTo(VerifyCodeRoute(email.value))
    }
}
