package com.cmtelematics.cmtreferenceapp.wrappers.crash

import android.net.Uri
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.CrashResponse
import com.cmtelematics.sdk.PassThruRequester
import com.cmtelematics.sdk.PassThruRequester.REQUEST_METHOD.PATCH
import kotlinx.coroutines.rx2.awaitFirst
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.time.Instant

internal class CrashServiceImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val passThruRequester: PassThruRequester
) : CrashService {
    override suspend fun updateCrash(crashId: Long, verified: Boolean, verifiedDate: Instant?) {
        Timber.i("Updating crash with crashId $crashId to backend")
        val url = Uri.parse(CRASH_UPDATE_URL)
            .buildUpon()
            .appendPath(crashId.toString())
            .build()
            .toString()

        val crashResponse = CrashResponse(verified = verified, verificationDateTime = verifiedDate)
        val crashResponseJson = json.encodeToString(crashResponse)
        try {
            withContext(dispatcherProvider.io) {
                passThruRequester
                    .request(PATCH, url, null, null, crashResponseJson, false)
                    .awaitFirst()
            }

            Timber.i("Successfully updated crash in backend for crashId: $crashId")
        } catch (e: Exception) {
            Timber.e(e, "Failure response from backend to update crash.")
            throw e
        }
    }

    companion object {
        private val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        private const val CRASH_UPDATE_URL = "/api/v1/crash_alerts"
    }
}
