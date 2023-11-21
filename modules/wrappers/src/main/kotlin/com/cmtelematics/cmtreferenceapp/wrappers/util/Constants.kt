package com.cmtelematics.cmtreferenceapp.wrappers.util

import androidx.datastore.preferences.core.booleanPreferencesKey

internal object Constants {
    val isTripSuppressionNotificationEnabled = booleanPreferencesKey("IS_TRIP_SUPPRESSION_NOTIFICATION_ENABLED")
}
