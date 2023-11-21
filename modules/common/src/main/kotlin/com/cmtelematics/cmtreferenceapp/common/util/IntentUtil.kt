package com.cmtelematics.cmtreferenceapp.common.util

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri

fun Activity.createSettingsIntent(): Intent =
    Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        addCategory(Intent.CATEGORY_DEFAULT)
        data = Uri.parse("package:$packageName")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    }

fun <T> Context.createActivityPendingIntent(
    activityClass: Class<T>,
    uri: String? = null
): PendingIntent? {
    val intent = Intent(
        this,
        activityClass
    ).apply {
        action = Intent.ACTION_VIEW
        uri?.run { data = toUri() }
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
    return TaskStackBuilder.create(this).run {
        addNextIntentWithParentStack(intent)
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}
