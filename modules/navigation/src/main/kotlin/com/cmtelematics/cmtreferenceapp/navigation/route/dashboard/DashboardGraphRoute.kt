package com.cmtelematics.cmtreferenceapp.navigation.route.dashboard

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class DashboardGraphRoute : Route(this) {

    companion object : Factory<DashboardGraphRoute>() {

        override val path = "DashboardGraph"

        override fun create(bundle: Bundle?) = DashboardGraphRoute()
    }
}
