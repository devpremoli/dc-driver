package com.cmtelematics.cmtreferenceapp.wrappers.crash

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import com.cmtelematics.cmtreferenceapp.common.util.createActivityPendingIntent
import com.cmtelematics.cmtreferenceapp.common.util.getCompatNotificationBuilder
import com.cmtelematics.cmtreferenceapp.wrappers.R
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashNotificationManager.RetryNotificationType
import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.CrashWithMetadata
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperNotificationParamsHolder
import com.cmtelematics.sdk.ServiceNotificationReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.distinctUntilChangedBy
import java.time.Clock
import java.time.Instant
import javax.inject.Inject

/**
 * Originally part of [CrashManager], all the notification handling was moved to this separate class.
 */
internal class CrashNotificationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val clock: Clock,
    private val alarmManager: AlarmManager,
    private val notificationManager: NotificationManager,
    private val notificationParamsHolder: WrapperNotificationParamsHolder,
    private val dataStore: DataStore<CrashWithMetadata?>
) : CrashNotificationManager {

    /**
     * When an alarm expires, just show the notification.
     */
    override fun handleNotificationRetryAlarmFired() {
        showCrashNotificationNow()
    }

    override suspend fun maintainNotificationScheduling() {
        dataStore.data
            .distinctUntilChangedBy { it?.run { crash.id } }
            .collect { crashWithMetadata ->
                if (crashWithMetadata == null) {
                    // Clear all notifications if the crash disappears for whatever reason.
                    cancelNotifications()
                } else if (crashWithMetadata.firstNotificationTime == null) {
                    // brand new crash, show notification immediately, schedule alarms, update lastNotification time
                    handleNotificationRetryAlarmFired()
                    val firstNotificationTime = clock.instant()
                    recreateRetryNotifications(firstNotificationTime)

                    dataStore.updateData { it?.copy(firstNotificationTime = firstNotificationTime) }
                } else {
                    // existing crash, updated, only ensure alarms are in place
                    // This will probably never be called as we distinct by crash id...
                    recreateRetryNotifications(crashWithMetadata.firstNotificationTime)
                }
            }
    }

    private fun showCrashNotificationNow() {
        createCrashNotificationBuilder()?.let { builder ->
            notificationManager.notify(NOTIFICATION_TAG, clock.millis().toInt(), builder.build())
        }
    }

    /*
     * Schedules notifications for every notification type in the enum.
     */
    private fun recreateRetryNotifications(firstNotificationTime: Instant) {
        cancelAlarms()
        RetryNotificationType.values().forEach { type ->
            val notificationTime = firstNotificationTime.plusMillis(type.delayMillis)
            if (notificationTime > clock.instant()) {
                scheduleRetryNotification(type, notificationTime.toEpochMilli())
            }
        }
    }

    private fun cancelNotifications() {
        notificationManager.activeNotifications
            .filter { it.tag == NOTIFICATION_TAG }
            .forEach { notificationManager.cancel(it.tag, it.id) }
        cancelAlarms()
    }

    private fun cancelAlarms() {
        RetryNotificationType.values().forEach { type ->
            makeRetryPendingIntent(type)?.let { alarmManager.cancel(it) }
        }
    }

    private fun scheduleRetryNotification(type: RetryNotificationType, triggerAtMillis: Long) {
        makeRetryPendingIntent(type)?.let { intent ->
            AlarmManagerCompat.setAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, triggerAtMillis, intent)
        }
    }

    private fun makeRetryPendingIntent(type: RetryNotificationType, extraFlags: Int = 0): PendingIntent? =
        PendingIntent.getBroadcast(
            context,
            RETRY_CRASH_NOTIFICATION_REQUEST,
            getRetryIntent(type),
            PendingIntent.FLAG_IMMUTABLE or extraFlags
        )

    private fun getRetryIntent(type: RetryNotificationType): Intent =
        Intent(context, RetryCrashNotificationReceiver::class.java)
            .apply { action = type.action }

    private fun createCrashNotificationBuilder(): NotificationCompat.Builder? =
        notificationParamsHolder.notificationParams?.let { notificationParams ->
            getCompatNotificationBuilder(context, ServiceNotificationReceiver.CMT_WARNING_CHANNEL)
                .setSmallIcon(notificationParams.notificationIcon)
                .setContentTitle(context.getString(R.string.service_crash_detected_title))
                .setContentText(context.getString(R.string.service_crash_detected_message))
                .setContentIntent(
                    context.createActivityPendingIntent(activityClass = notificationParams.mainActivityClass)
                )
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

    companion object {
        private const val NOTIFICATION_TAG = "RefAppCrashNotifications"
        private const val RETRY_CRASH_NOTIFICATION_REQUEST = 1006
    }
}
