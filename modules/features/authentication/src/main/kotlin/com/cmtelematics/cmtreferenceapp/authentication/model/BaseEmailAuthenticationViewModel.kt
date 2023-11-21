package com.cmtelematics.cmtreferenceapp.authentication.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.util.isValidEmail
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

abstract class BaseEmailAuthenticationViewModel(
    navigator: Navigator,
    errorService: ErrorService,
    private val dataStore: DataStore<Preferences>,
    private val dispatcherProvider: DispatcherProvider
) : BaseScreenViewModel(navigator, errorService) {

    val email = MutableStateFlow("")

    val submitEnabled = email.map { it.isValidEmail() }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = false)

    protected val latestEmailPrefsKey = stringPreferencesKey("LATEST_EMAIL_PREF")

    /**
     * Call the specific SDK method when implementing this method. Errors will be handled by the base class.
     */
    protected abstract suspend fun submitEmail()

    fun submit() = launchWithErrorHandling {
        trackProgress {
            saveCurrentEmail()
            submitEmail()
        }
    }

    private suspend fun saveCurrentEmail() = withContext(dispatcherProvider.io) {
        dataStore.edit { preferences ->
            preferences[latestEmailPrefsKey] = email.value
        }
    }
}
