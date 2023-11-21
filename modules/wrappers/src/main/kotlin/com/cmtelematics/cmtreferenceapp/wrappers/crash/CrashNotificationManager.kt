package com.cmtelematics.cmtreferenceapp.wrappers.crash

import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MINUTES

/**
 * Utility class that observers active crashes and schedules reminder notifications as appropriate.
 */
internal interface CrashNotificationManager {

    /**
     * Daemon function that observers active crashes and schedules reminders. Never returns.
     */
    suspend fun maintainNotificationScheduling()

    /**
     * When a reminder Alarm expires, this method should be called to complete the reminder logic.
     */
    fun handleNotificationRetryAlarmFired()

    /**
     * Reminder type enums.
     */
    enum class RetryNotificationType(val action: String, val delayMillis: Long) {
        First(ACTION_RETRY_CRASH_FIRST_NOTIFICATION, MINUTES.toMillis(FIRST_RETRY_NOTIFICATION_DELAY_IN_MINUTES)),
        Second(ACTION_RETRY_CRASH_SECOND_NOTIFICATION, HOURS.toMillis(SECOND_RETRY_NOTIFICATION_DELAY_IN_HOURS))
    }
}

private const val FIRST_RETRY_NOTIFICATION_DELAY_IN_MINUTES = 5L
private const val SECOND_RETRY_NOTIFICATION_DELAY_IN_HOURS = 1L

private const val ACTION_RETRY_CRASH_FIRST_NOTIFICATION = "ACTION_RETRY_CRASH_FIRST_NOTIFICATION"
private const val ACTION_RETRY_CRASH_SECOND_NOTIFICATION = "ACTION_RETRY_CRASH_SECOND_NOTIFICATION"
