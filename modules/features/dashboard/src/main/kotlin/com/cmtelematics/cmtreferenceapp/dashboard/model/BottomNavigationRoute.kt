package com.cmtelematics.cmtreferenceapp.dashboard.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.cmtelematics.cmtreferenceapp.navigation.Route

internal data class BottomNavigationRoute(
    val routeFactory: Route.Factory<*>,
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int
)
