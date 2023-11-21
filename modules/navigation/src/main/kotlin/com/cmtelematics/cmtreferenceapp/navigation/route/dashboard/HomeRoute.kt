package com.cmtelematics.cmtreferenceapp.navigation.route.dashboard

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class HomeRoute : Route(this) {

    companion object : Factory<HomeRoute>() {

        override val path = "home"

        override fun create(bundle: Bundle?) = HomeRoute()
    }
}
