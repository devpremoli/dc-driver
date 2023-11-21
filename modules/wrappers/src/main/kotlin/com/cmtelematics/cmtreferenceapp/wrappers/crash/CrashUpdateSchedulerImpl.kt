package com.cmtelematics.cmtreferenceapp.wrappers.crash

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.InternalConstants.CRASH_ID_ARG
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.InternalConstants.CRASH_RESPONSE_ARG
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.InternalConstants.CRASH_RESPONSE_DATE_ARG
import timber.log.Timber
import java.time.Instant
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class CrashUpdateSchedulerImpl @Inject constructor(
    private val workManager: WorkManager
) : CrashUpdateScheduler {
    override fun scheduleCrashUpdate(crashId: Long, confirmed: Boolean, confirmationTime: Instant) {
        Timber.i("Updating crash with id $crashId")

        val data = Data.Builder()
            .putLong(CRASH_ID_ARG, crashId)
            .putBoolean(CRASH_RESPONSE_ARG, confirmed)
            .putLong(CRASH_RESPONSE_DATE_ARG, confirmationTime.toEpochMilli())
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val crashUpdateRequest = OneTimeWorkRequest.Builder(CrashUpdateWorker::class.java)
            .addTag(crashId.toString())
            .setConstraints(constraints)
            .setInitialDelay(CRASH_UPDATE_INITIAL_DELAY, TimeUnit.SECONDS)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_DELAY_MIN, TimeUnit.MINUTES)
            .setInputData(data)
            .build()

        workManager.beginUniqueWork(crashId.toString(), ExistingWorkPolicy.REPLACE, crashUpdateRequest).enqueue()
    }

    companion object {
        private const val BACKOFF_DELAY_MIN = 30L
        private const val CRASH_UPDATE_INITIAL_DELAY = 15L
    }
}
