package com.cmtelematics.cmtreferenceapp.wrappers.authentication.util

import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun getUserLoggedOutFlow(startupManager: StartupManager, authenticationManager: AuthenticationManager) =
    startupManager.isAppInitialised()
        .toAuthStateFlow(authenticationManager)
        // Skip the default state
        .drop(1)
        .map { it is AuthenticationManager.AuthenticationState.LoggedIn }
        .distinctUntilChanged()
        .filter { !it }

private fun Flow<Boolean>.toAuthStateFlow(authenticationManager: AuthenticationManager) =
    flatMapLatest { isInitialized ->
        if (isInitialized) {
            authenticationManager.state
        } else {
            flow { awaitCancellation() }
        }
    }
