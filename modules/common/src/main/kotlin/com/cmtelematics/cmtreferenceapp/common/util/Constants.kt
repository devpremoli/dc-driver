package com.cmtelematics.cmtreferenceapp.common.util

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {

    private const val UNIQUE_ORDINAL_DAY_START_RANGE = 11
    private const val UNIQUE_ORDINAL_DAY_END_RANGE = 13
    val UNIQUE_ORDINAL_DAYS = UNIQUE_ORDINAL_DAY_START_RANGE..UNIQUE_ORDINAL_DAY_END_RANGE

    const val DAY_OF_MONTH_DIVISOR = 10
    const val FIRST_DAY_OF_MONTH = 1
    const val SECOND_DAY_OF_MONTH = 2
    const val THIRD_DAY_OF_MONTH = 3

    const val VERSION_NAME = "VERSION_NAME"
    const val VERSION_CODE = "VERSION_CODE"

    val isOnboardingFlowCompleted = booleanPreferencesKey("IS_ONBOARDING_FLOW_COMPLETED")
}
