package com.cmtelematics.cmtreferenceapp.wrappers.service

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.cmtelematics.cmtreferenceapp.wrappers.R
import com.cmtelematics.cmtreferenceapp.wrappers.service.model.WrapperNotificationParams
import com.cmtelematics.sdk.CmtService
import com.cmtelematics.sdk.CmtServiceListener
import com.cmtelematics.sdk.ServiceUtils
import com.cmtelematics.sdk.SleepySamsungHelper
import com.cmtelematics.sdk.types.NonStartReasons.AIRPLANE_MODE
import com.cmtelematics.sdk.types.NonStartReasons.BT_DISABLED
import com.cmtelematics.sdk.types.NonStartReasons.LOW_BATTERY
import com.cmtelematics.sdk.types.NonStartReasons.POWER_SAVE
import com.cmtelematics.sdk.types.NonStartReasons.STANDBY_ENABLED
import com.cmtelematics.sdk.types.ServiceState
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_IDLE
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_IDLE_ACTIVITY_RECOGNITION_PERMISSION_MISSING
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_IDLE_BLUETOOTH_PERMISSION_MISSING
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_IDLE_LOCATION_DISABLED
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_IDLE_LOCATION_PERMISSION_MISSING
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_IDLE_TAG_MODE_LOW_BATTERY
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_IDLE_TAG_MODE_POWER_SAVE
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_IDLE_TAG_MODE_STANDBY
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_IMPACT
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_RECORDING
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_RECORDING_REDUCED_POWER
import com.cmtelematics.sdk.types.ServiceState.ACTIVE_SEARCHING
import com.cmtelematics.sdk.types.ServiceState.NO_AUTH
import com.cmtelematics.sdk.types.ServiceState.ON_DESTROY
import com.cmtelematics.sdk.types.ServiceState.SUSPENDED

/**
 * Listener to configure notifications for the app.
 *
 * [Docs](https://my-cmt-alpha.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-sdk/drivewell-sdk-for-android/setting-up-the-drivewell-sdk-for-android/subclassing-services,-listeners,-and-receivers--android-.html)
 */
internal class ReferenceAppTelematicsListener(
    private val context: CmtService,
    private val notificationParams: WrapperNotificationParams
) : CmtServiceListener(context) {
    override fun getNotificationChannelId(context: Context): String = CHANNEL_ID

    override fun getNotificationChannelName(context: Context): CharSequence =
        context.getString(R.string.notification_channel_name)

    override fun getNotificationChannelDescription(context: Context): String =
        context.getString(R.string.notification_channel_name)

    override fun getNotificationId(): Int = 123123

    override fun getFleetNotification(state: ServiceState): Notification = getNotification(state)

    @Suppress("ComplexMethod", "LongMethod")
    override fun getNotification(state: ServiceState): Notification {
        // First, determine the notification subtitle text to show and figure out an appropriate intent that tapping
        // the notification will invoke. If the intent is null, we will just show the main activity of the app
        // specified in [notificationParams].

        val nonStartReasons = ServiceUtils.getNonStartReasons(context)
        val (subtitleRes: Int?, intent: Intent?) = when (state) {
            SUSPENDED -> when {
                // Note: This should come before BT_DISABLED because activating airplane mode
                // typically also disables bluetooth.
                AIRPLANE_MODE in nonStartReasons ->
                    R.string.service_notification_suspended_airplane_mode to Intent(Settings.ACTION_SETTINGS)
                // Allow the user to enable Bluetooth directly from the FG service notification.
                BT_DISABLED in nonStartReasons ->
                    R.string.service_notification_bluetooth_warning to
                        Intent(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                // If in standby, just open the app if the user taps on the notification.
                // An alternative implementation would allow the user to extend or cancel standby mode from here.
                STANDBY_ENABLED in nonStartReasons ->
                    R.string.service_notification_standby to null
                POWER_SAVE in nonStartReasons ->
                    R.string.service_notification_suspended_power_save to
                        SleepySamsungHelper.get(context).batterySettingsIntent
                LOW_BATTERY in nonStartReasons ->
                    R.string.service_notification_low_battery to
                        SleepySamsungHelper.get(context).batterySettingsIntent
                nonStartReasons.isEmpty() -> R.string.service_notification_no_reason_for_suspension to null
                // Trip recording is suspended for some other reason that needs to be resolved in the app.
                // For example, the current date could be before trip recording is scheduled to begin.

                // Developers may chose to display other text here when trip recording is suspended,
                // although it is most likely simplest to just direct the user into the app for resolution.
                else -> R.string.service_notification_unknown_suspension to null
            }
            ACTIVE_RECORDING, ACTIVE_IMPACT -> R.string.service_notification_recording to null
            // Note: this only occurs in Tag mode
            ACTIVE_RECORDING_REDUCED_POWER -> R.string.service_notification_recording_reduced_power to null
            // The Service is in an idle state. GPS is off.
            ACTIVE_IDLE -> R.string.service_notification_idle to null
            ACTIVE_IDLE_LOCATION_DISABLED ->
                R.string.service_notification_degraded_location to
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            // Launch the UI which, in turn, should detect the missing permission and prompt the user
            // I.e. no special intent is necessary.
            ACTIVE_IDLE_LOCATION_PERMISSION_MISSING ->
                R.string.service_notification_degraded_location_permission to null
            // Same as missing location permission.
            ACTIVE_IDLE_ACTIVITY_RECOGNITION_PERMISSION_MISSING ->
                R.string.service_notification_degraded_activity_recognition_permission to null
            // In the following three states, the SDK will connect to tags, but will not activate GPS.
            // After connecting to a tag, we will record the trip in ACTIVE_RECORDING_REDUCED_POWER state.
            ACTIVE_IDLE_TAG_MODE_POWER_SAVE ->
                R.string.service_notification_idle_tag_mode_power_save to
                    SleepySamsungHelper.get(context).batterySettingsIntent
            ACTIVE_IDLE_TAG_MODE_LOW_BATTERY ->
                R.string.service_notification_idle_tag_mode_low_battery to
                    SleepySamsungHelper.get(context).batterySettingsIntent
            // Launch the UI which should show the user that standby is enabled.
            // Alternatively provide an action here which will disable standby directly from the notification.
            ACTIVE_IDLE_TAG_MODE_STANDBY -> R.string.service_notification_standby to null
            // Note: GPS has been activated to further sense if a user is driving.
            ACTIVE_SEARCHING -> R.string.service_notification_active_searching to null
            // Note: this state exists only briefly because the Service exits after de-authorization.
            NO_AUTH -> R.string.service_notification_no_auth to null
            ACTIVE_IDLE_BLUETOOTH_PERMISSION_MISSING, ON_DESTROY -> null to null
        }

        val subtitle = subtitleRes?.let { context.getString(it) }.orEmpty()
        val pendingIntent = if (intent != null) {
            // The user can take some specific action, e.g. turn on BT, that we can trigger here.
            // execute that action when the user taps on the fg service notification.
            PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
        } else {
            // open the UI when the user taps on the fg service notification.
            val nextIntent = Intent(context, notificationParams.mainActivityClass)
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addNextIntent(nextIntent)
            stackBuilder.getPendingIntent(0, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
        }
        val builder: NotificationCompat.Builder = notificationBuilder
            .setContentTitle(context.getString(notificationParams.notificationTitle))
            .setContentText(subtitle)
            .setSmallIcon(notificationParams.notificationIcon)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(subtitle)
            )
            .setContentIntent(pendingIntent)

        return builder.build()
    }

    companion object {
        private const val CHANNEL_ID = "CHANNEL_SERVICE"
    }
}
