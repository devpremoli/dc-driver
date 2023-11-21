package com.cmtelematics.cmtreferenceapp.wrappers.driver

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SessionDataStore
import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.util.getUserLoggedOutFlow
import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.CrashWithMetadata
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LogoutDataStoreClearDriverImpl @Inject constructor(
    @SessionDataStore
    private val dataStore: DataStore<Preferences>,
    private val crashDataStore: DataStore<CrashWithMetadata?>,
    private val authenticationManager: AuthenticationManager,
    private val startupManager: StartupManager
) : LogoutDataStoreClearDriver {
    override suspend fun run() {
        getUserLoggedOutFlow(startupManager, authenticationManager)
            .onEach {
                crashDataStore.updateData { null }
                dataStore.edit { preferences ->
                    preferences.clear()
                }
            }
            .collect()
    }
}
