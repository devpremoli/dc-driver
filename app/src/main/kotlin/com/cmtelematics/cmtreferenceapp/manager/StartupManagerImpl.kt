package com.cmtelematics.cmtreferenceapp.manager

import com.cmtelematics.cmtreferenceapp.R
import com.cmtelematics.cmtreferenceapp.common.manager.AlertManager
import com.cmtelematics.cmtreferenceapp.common.manager.AlertType
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class StartupManagerImpl @Inject constructor(
    private val permissionManager: PermissionManager,
    private val dispatcherProvider: DispatcherProvider,
    private val alertManager: AlertManager
) : StartupManager {

    private val initialized = MutableStateFlow(false)

    override fun isAppInitialised(): Flow<Boolean> = initialized

    override suspend fun initializeApp() {
        safeScope {
            launch { alertManagerInit() }
            launch { permissionManager.refresh() }
            // add additional preloading
        }

        initialized.value = true
    }

    private suspend fun safeScope(block: suspend CoroutineScope.() -> Unit) {
        val exceptionHandler = CoroutineExceptionHandler { _, error ->
            Timber.e(error)
        }

        withContext(dispatcherProvider.default + exceptionHandler) {
            supervisorScope {
                block()
            }
        }
    }

    /**
     * AlertManager initializer method
     */
    private suspend fun alertManagerInit() {
        alertManager.initialize(
            mapOf(
                AlertType.HardBraking to R.raw.hard_brake_alert
                // add additional alert types here
            )
        )
    }
}
