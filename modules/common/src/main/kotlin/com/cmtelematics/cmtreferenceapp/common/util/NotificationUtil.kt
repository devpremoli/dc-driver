package com.cmtelematics.cmtreferenceapp.common.util

import android.content.Context
import androidx.core.app.NotificationCompat

fun getCompatNotificationBuilder(context: Context, channel: String) = NotificationCompat.Builder(context, channel)
