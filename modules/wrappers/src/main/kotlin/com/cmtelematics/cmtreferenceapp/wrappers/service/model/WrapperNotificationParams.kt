package com.cmtelematics.cmtreferenceapp.wrappers.service.model

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Configuration parameters needed to configure Notifications.
 */
data class WrapperNotificationParams(
    /**
     * The icon to use for the notifications.
     */
    @DrawableRes val notificationIcon: Int,

    /**
     * The title to use for the notifications.
     */
    @StringRes val notificationTitle: Int,

    /**
     * A string resource for the App's name.
     */
    @StringRes val appNameRes: Int,

    /**
     * The class representing the main activity of the app. Used as an Intent destination for some notifications.
     */
    val mainActivityClass: Class<out Activity>
)
