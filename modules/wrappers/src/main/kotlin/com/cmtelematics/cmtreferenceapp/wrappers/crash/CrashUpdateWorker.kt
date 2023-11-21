package com.cmtelematics.cmtreferenceapp.wrappers.crash

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.InternalConstants.CRASH_ID_ARG
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.InternalConstants.CRASH_RESPONSE_ARG
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.InternalConstants.CRASH_RESPONSE_DATE_ARG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.time.Instant

/**
 * Worker that uses [CrashService] to update a crash on the backend. Has maximum retry logic built in.
 */
@HiltWorker
internal class CrashUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val crashService: CrashService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val crashId = inputData.getLong(CRASH_ID_ARG, 0)
        val confirmed = inputData.getBoolean(CRASH_RESPONSE_ARG, true)
        val respondedInstantMillis = inputData.getLong(CRASH_RESPONSE_DATE_ARG, 0L)

        return try {
            crashService.updateCrash(crashId, confirmed, Instant.ofEpochMilli(respondedInstantMillis))

            val outputData = Data.Builder()
                .putLong(CRASH_ID_ARG, crashId)
                .build()
            Result.success(outputData)
        } catch (exception: Exception) {
            Timber.e(exception, "Failed to update crash.")

            if (runAttemptCount == MAX_REQUEST_ATTEMPT) {
                Result.failure()
            } else {
                Result.retry()
            }
        }
    }

    companion object {
        private const val MAX_REQUEST_ATTEMPT = 5
    }
}
