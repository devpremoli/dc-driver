package com.cmtelematics.cmtreferenceapp.wrappers.push

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import com.cmtelematics.cmtreferenceapp.wrappers.device.SettingsManager
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperNotificationParamsHolder
import com.cmtelematics.sdk.DefaultCoreEnv
import com.cmtelematics.sdk.NotificationHelper
import com.cmtelematics.sdk.PushMessage
import com.cmtelematics.sdk.PushMessageIntentService
import com.cmtelematics.sdk.ServiceNotificationReceiver.CMT_WARNING_CHANNEL
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.Clock
import javax.inject.Inject

/**
 * [Docs](https://my.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-sdk/drivewell-sdk-for-android/integrating-the-drivewell-sdk--android-/enabling-push-notifications--android-.html)
 */
@AndroidEntryPoint
internal class ReferenceAppPushService : PushMessageIntentService() {
    @Inject
    internal lateinit var deviceSettingsManager: SettingsManager

    @Inject
    internal lateinit var paramsHolder: WrapperNotificationParamsHolder

    @Inject
    internal lateinit var notificationManager: NotificationManager

    @Inject
    internal lateinit var clock: Clock

    @Inject
    internal lateinit var coreEnv: DefaultCoreEnv

    private val notificationParams
        get() = paramsHolder.notificationParams ?: error(
            """
                Telematics service was started before notification params were specified.
                Was SDK properly initialized?
                """
                .trimIndent()
        )

    override fun onNewToken(token: String) {
        deviceSettingsManager.setFirebaseToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (processCmtActionFromMessage(message)) {
            Timber.d("A silent push message was processed successfully, message: %s", message)
            return
        }

        val env = DefaultCoreEnv(this.applicationContext)
        val cmtMessage = PushMessage.newInstance(message, env.configuration.deviceID, env.userManager.userID)
        if (cmtMessage == null) {
            // insert your custom code here
            Timber.d("Non CMT message received, message: %s", message)
        } else {
            onCmtMessageReceived(cmtMessage, message.data, message.collapseKey)
        }
    }

    override fun getNotificationHelper(context: Context): NotificationHelper = object : NotificationHelper(context) {

        override fun setupNotificationChannel() {
            // this code may not be required if you already set up the notification channel elsewhere
        }

        override fun getNotificationChannelId() = CMT_WARNING_CHANNEL

        override fun getContentTitle() = context.getString(notificationParams.notificationTitle)

        override fun getIcon() = notificationParams.notificationIcon

        override fun getMainActivityClass(): Class<out Activity> = notificationParams.mainActivityClass
    }
}
