package com.cmtelematics.cmtreferenceapp.wrappers.service

import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.DriverScope
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SessionDataStore
import com.cmtelematics.cmtreferenceapp.wrappers.R
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperNotificationParamsHolder
import com.cmtelematics.cmtreferenceapp.wrappers.service.model.WrapperNotificationParams
import com.cmtelematics.cmtreferenceapp.wrappers.util.Constants.isTripSuppressionNotificationEnabled
import com.cmtelematics.sdk.CmtService
import com.cmtelematics.sdk.ServiceNotificationReceiver
import com.cmtelematics.sdk.types.DriveDetectorType
import com.cmtelematics.sdk.types.DriveDetectorType.EXTERNAL_WITH_TAG
import com.cmtelematics.sdk.types.ServiceConstants.GOOGLE_PLAY_SERVICES_CONNECTION_SUSPENDED
import com.cmtelematics.sdk.types.ServiceNotificationType
import com.cmtelematics.sdk.types.ServiceNotificationType.ACCEL_FAILURE
import com.cmtelematics.sdk.types.ServiceNotificationType.ACTIVITY_RECOGNITION_PERMISSION_MISSING
import com.cmtelematics.sdk.types.ServiceNotificationType.BACKGROUND_RESTRICTED
import com.cmtelematics.sdk.types.ServiceNotificationType.BLUETOOTH_PERMISSION_MISSING
import com.cmtelematics.sdk.types.ServiceNotificationType.BTLE_DISABLED
import com.cmtelematics.sdk.types.ServiceNotificationType.BTLE_TAG_IMPACT_ALERT
import com.cmtelematics.sdk.types.ServiceNotificationType.GOOGLE_PLAY_SERVICE
import com.cmtelematics.sdk.types.ServiceNotificationType.GPS
import com.cmtelematics.sdk.types.ServiceNotificationType.GPS_FAILURE
import com.cmtelematics.sdk.types.ServiceNotificationType.GPS_PERMISSION
import com.cmtelematics.sdk.types.ServiceNotificationType.GYRO_FAILURE
import com.cmtelematics.sdk.types.ServiceNotificationType.NETLOC
import com.cmtelematics.sdk.types.ServiceNotificationType.NETLOC_PERMISSION
import com.cmtelematics.sdk.types.ServiceNotificationType.PANIC_BUTTON
import com.cmtelematics.sdk.types.ServiceNotificationType.STANDBY_MODE
import com.cmtelematics.sdk.types.ServiceNotificationType.SUSPENDED_LOW_BATTERY
import com.cmtelematics.sdk.types.ServiceNotificationType.SUSPENDED_POWER_SAVE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

/**
 * The [ServiceNotificationReceiver], also referred to as the Anomaly Receiver in the docs is a Broadcast receiver
 * that the SDK notifies of recording-related transient issues. Importantly, this receiver handles issues that are
 * separate from the Foreground Service notification. The Foreground Service notification is the sole responsibility
 * of [ReferenceAppTelematicsListener].
 *
 * In this receiver, the developer can choose how to notify the user of anomalies, whether to show notifications
 * or to display these anomalies in an in-app UI experience.
 *
 * Here we've gone with the relatively simpler approach of displaying local notifications.
 */
@AndroidEntryPoint
internal class ServiceAnomalyReceiver : ServiceNotificationReceiver() {
    @Inject
    internal lateinit var notificationParamsHolder: WrapperNotificationParamsHolder

    @SessionDataStore
    @Inject
    internal lateinit var preferences: DataStore<Preferences>

    @DriverScope
    @Inject
    internal lateinit var driverScope: CoroutineScope

    private val notificationParams: WrapperNotificationParams by lazy {
        notificationParamsHolder.notificationParams ?: error(
            """
            Telematics service was started before notification params were specified.
            Was SDK properly initialized?
            """.trimIndent()
        )
    }

    override fun getStartMainActivityIntent(): Intent {
        // open the UI when the user taps on the fg service notification.
        val mainActivityClass = notificationParams.mainActivityClass
        return Intent(context, mainActivityClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    }

    override fun onReceive(context: Context?, intent: Intent) {
        // Note: since onReceive is just logging, overriding this call is unnecessary.
        // However, if you do, make sure to call the overridden method.
        Timber.v("onReceive intent $intent")
        super.onReceive(context, intent)
    }

    /**
     *  The default implementation of [display] will generate a local notification using [getDescription] to fill
     *  out the details. If you instead want to implement an in-app UX for displaying anomalies or something else, then
     *  don't call super's method, just implement your own logic here.
     */
    override fun display(type: ServiceNotificationType, code: Int) {
        Timber.v("display type=$type code=$code")
        // Make sure the overridden method is called so that the other ServiceNotificationTypes are displayed.
        driverScope.launch {
            val isTripSuppressionNotificationEnabled =
                preferences.data.first()[isTripSuppressionNotificationEnabled] == true

            if (isTripSuppressionNotificationEnabled) {
                super.display(type, code)
            }
        }
    }

    /**
     * While it's mandatory to override this method, [getDescription] is only used by super's implementation of
     * [display]. If you're not using super.display to show notifications to the user, you can just return null from
     * this method.
     */
    @Suppress("LongMethod", "ComplexMethod")
    override fun getDescription(type: ServiceNotificationType?, code: Int?): NotificationDesc? {
        Timber.i("Creating description for service notification type: '%s' and code %s", type, code)

        val serviceName = context.getString(R.string.app_name)

        // See [ReferenceAppTelematicsListener].
        // If the Service is running, the content of the foreground service banner can be changed
        // to allow the user to resolve the issue. We don't need to display two separate notifications
        // that handle the same problem.
        val transientNotificationTypes = listOf(
            GPS_PERMISSION,
            GPS,
            NETLOC,
            BTLE_DISABLED,
            SUSPENDED_POWER_SAVE,
            SUSPENDED_LOW_BATTERY,
            STANDBY_MODE
        )
        if (CmtService.isRunning() && type in transientNotificationTypes) {
            Timber.i("Skipping display of transient notification $type because service is running.")
            return null
        }

        val warningResId: Int = notificationParams.notificationIcon

        return when (type) {
            GPS_PERMISSION -> NotificationDesc(
                context.getString(R.string.permission_required),
                context.getString(R.string.service_requires_location_permissions, serviceName),
                CMT_WARNING_CHANNEL,
                warningResId,
                true,
                GPS_PERMISSION_NOTIFICATION_ID
            )
            ACTIVITY_RECOGNITION_PERMISSION_MISSING -> NotificationDesc(
                context.getString(R.string.permission_required),
                context.getString(R.string.service_requires_activity_recognition_permissions, serviceName),
                CMT_WARNING_CHANNEL,
                warningResId,
                true,
                ACTIVITY_RECOGNITION_PERMISSION_NOTIFICATION_ID
            )
            GPS -> NotificationDesc(
                context.getString(R.string.location_service_required),
                context.getString(R.string.service_requires_gps, serviceName),
                CMT_WARNING_CHANNEL,
                warningResId,
                true,
                GPS_ENABLED_NOTIFICATION_ID
            )
            NETLOC -> NotificationDesc(
                context.getString(R.string.location_service_required),
                context.getString(R.string.service_requires_netloc, serviceName),
                CMT_WARNING_CHANNEL,
                warningResId,
                true,
                GOOGLE_LOCATION_ENABLED_NOTIFICATION_ID
            )
            GOOGLE_PLAY_SERVICE -> {
                val titleResId = if (code == GOOGLE_PLAY_SERVICES_CONNECTION_SUSPENDED) {
                    R.string.google_play_services_title_suspended
                } else {
                    R.string.google_play_services_title_failure
                }
                val titleStr: String = context.getString(titleResId)
                NotificationDesc(
                    titleStr,
                    context.getString(R.string.google_play_services_message),
                    CMT_WARNING_CHANNEL,
                    warningResId,
                    true,
                    GOOGLE_PLAY_ENABLED_NOTIFICATION_ID
                )
            }
            BTLE_DISABLED -> if (
                configuration.activeDriveDetector in listOf(DriveDetectorType.TAG, EXTERNAL_WITH_TAG)
            ) {
                NotificationDesc(
                    context.getString(R.string.btle_required),
                    context.getString(R.string.service_requires_btle, serviceName),
                    CMT_WARNING_CHANNEL,
                    warningResId,
                    true,
                    BTLE_DISABLED_NOTIFICATION_ID
                )
            } else {
                null
            }
            GPS_FAILURE -> NotificationDesc(
                context.getString(R.string.location_service_error_title),
                context.getString(R.string.location_service_error_content),
                CMT_WARNING_CHANNEL,
                warningResId,
                true,
                GPS_ERROR_NOTIFICATION_ID
            )
            STANDBY_MODE -> {
                val standbyMsg = if (code != null) {
                    val hours: Int = code.minutes.inWholeHours.toInt()
                    context.resources.getQuantityString(
                        R.plurals.service_notification_standby_for_x_hours,
                        hours,
                        hours
                    )
                } else {
                    context.getString(R.string.service_notification_standby)
                }
                NotificationDesc(
                    serviceName,
                    standbyMsg,
                    CMT_WARNING_CHANNEL,
                    notificationParams.notificationIcon,
                    true,
                    STANDBY_MODE_NOTIFICATION_ID
                )
            }
            SUSPENDED_POWER_SAVE -> NotificationDesc(
                context.getString(R.string.trip_recording_suspended),
                context.getString(R.string.trip_recording_suspended_power_save, serviceName),
                CMT_WARNING_CHANNEL,
                warningResId,
                true,
                POWER_SAVE_MODE_ID
            )
            SUSPENDED_LOW_BATTERY -> NotificationDesc(
                context.getString(R.string.trip_recording_suspended),
                context.getString(R.string.trip_recording_suspended_low_battery, serviceName),
                CMT_WARNING_CHANNEL,
                warningResId,
                true,
                LOW_BATTERY_MODE_ID
            )
            BACKGROUND_RESTRICTED -> NotificationDesc(
                context.getString(R.string.trip_recording_suspended),
                String.format(
                    context.getString(R.string.trip_recording_suspended_background_restricted),
                    context.getString(notificationParams.appNameRes),
                    context.getString(notificationParams.appNameRes)
                ),
                CMT_WARNING_CHANNEL,
                warningResId,
                true,
                BACKGROUND_RESTRICTED_ID
            )
            ACCEL_FAILURE,
            GYRO_FAILURE -> NotificationDesc(
                context.getString(R.string.trip_recording_failure),
                context.getString(R.string.trip_recording_failure_reason, type),
                CMT_WARNING_CHANNEL,
                warningResId,
                true,
                when (type) {
                    GYRO_FAILURE -> GYRO_ERROR_NOTIFICATION_ID
                    ACCEL_FAILURE -> ACCEL_ERROR_NOTIFICATION_ID
                    else -> 0
                }
            )
            NETLOC_PERMISSION,
            BTLE_TAG_IMPACT_ALERT,
            PANIC_BUTTON,
            BLUETOOTH_PERMISSION_MISSING,
            null -> null
        }
    }
}
