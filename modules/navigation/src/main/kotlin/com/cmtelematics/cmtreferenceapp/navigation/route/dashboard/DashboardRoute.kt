package com.cmtelematics.cmtreferenceapp.navigation.route.dashboard

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class DashboardRoute : Route(this) {

    companion object : Factory<DashboardRoute>() {

        override val path = "dashboard"

        override fun create(bundle: Bundle?) = DashboardRoute()
    }
}
