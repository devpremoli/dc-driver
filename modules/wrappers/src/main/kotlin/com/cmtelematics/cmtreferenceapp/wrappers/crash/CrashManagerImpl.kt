package com.cmtelematics.cmtreferenceapp.wrappers.crash

import androidx.datastore.core.DataStore
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.isLoggedIn
import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.Crash
import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.CrashWithMetadata
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.generateFakeCrash
import dagger.Lazy
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.time.Clock
import java.util.concurrent.TimeUnit.HOURS
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Docs](https://my.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-crash---claims.html)
 *
 * Manages the crash & crash flow states for the App. Per requirements, the app can only handle a single crash at a
 * time. If a second crash is detected while there's already one active, it is either discarded or replaces the
 * active crash depending on whether it has higher priority (is higher in intensity or is more recent). Crash
 * state is continually persisted via Android DataStore.
 */
@Singleton
internal class CrashManagerImpl @Inject constructor(
    private val authenticationManager: Lazy<AuthenticationManager>,
    private val crashUpdateScheduler: CrashUpdateScheduler,
    private val dataStore: DataStore<CrashWithMetadata?>,
    private val clock: Clock
) : CrashManager, CrashBroadcastManager {

    // though this is a singleton class, we add mutex logic for extra safety.
    private val mutex = Mutex()

    private val crashWithMetadata = dataStore.data
        .catch { throwable ->
            Timber.e(throwable, "Couldn't load crash data from disk.")
            emit(null)
        }

    override val activeCrash = crashWithMetadata
        .map { it?.crash }

    override val firstNotificationTime = crashWithMetadata
        .map { it?.firstNotificationTime }

    private val Crash.isValid: Boolean get() = verified == true && intensity >= CRASH_INTENSITY_THRESHOLD && !isExpired

    private val Crash.isExpired: Boolean
        get() = timestamp?.let { crashTime ->
            return clock.millis() - crashTime.toEpochMilli() > HOURS.toMillis(24)
        } ?: true

    /*
     * When there's a new crash detected, we have to check whether it is more important than the crash we already have.
     * Also, we don't handle crashes when the user is not logged in.
     */
    override suspend fun handleCrashFromBroadcast(crash: Crash) = mutex.withLock {
        if (authenticationManager.get().isLoggedIn() && crash.isValid) {
            val currentCrashMetadata = crashWithMetadata.first()
            val currentCrash = currentCrashMetadata?.crash
            val flowInProgress = currentCrashMetadata?.flowStarted == true

            // Could be optimized by combining all terms into a single expression, but this is easier to read and
            // has negligible impact on performance.
            val isNewCrash = currentCrash == null
            val crashUpdated = crash.id == currentCrash?.id
            val canAcceptMoreImportantCrash = currentCrash != null && crash > currentCrash && !flowInProgress

            if (isNewCrash || crashUpdated || canAcceptMoreImportantCrash) {
                Timber.i("Storing new(er) crash: %s", crash)
                dataStore.updateData { storedMetadata ->
                    storedMetadata?.copy(
                        crash = crash,
                        // Reset the first notification time if this is not an update to an existing crash.
                        firstNotificationTime = if (crashUpdated) storedMetadata.firstNotificationTime else null
                    ) ?: CrashWithMetadata(crash = crash)
                }
            } else {
                Timber.i("Crash from broadcast less important than current crash, dropping: %s", crash)
            }
        }
    }

    /**
     * If the user has made a selection, we enqueue a WorkManager Worker to update the details on the backend. We
     * clear the crash from the active crash variable, this also has the extra effect that the reminder notitification
     * will be cancelled. See [CrashNotificationManager].
     */
    override suspend fun handleUserSelection(crashConfirmed: Boolean, shouldSendAmbulance: Boolean): Unit =
        mutex.withLock {
            // ambulance response is not supported by backend...
            activeCrash.first()?.let { crash ->
                crashUpdateScheduler.scheduleCrashUpdate(crash.id, crashConfirmed, clock.instant())
            }
            dataStore.updateData { null }
        }

    override suspend fun simulateCrash() = handleCrashFromBroadcast(generateFakeCrash(clock))

    override suspend fun notifyFlowStarted(): Unit = mutex.withLock {
        dataStore.updateData { it?.copy(flowStarted = true) }
    }

    companion object {
        private const val CRASH_INTENSITY_THRESHOLD = 1
    }
}
