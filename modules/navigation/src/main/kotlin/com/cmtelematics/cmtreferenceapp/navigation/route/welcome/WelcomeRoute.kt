package com.cmtelematics.cmtreferenceapp.navigation.route.welcome

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class WelcomeRoute : Route(this) {

    companion object : Factory<WelcomeRoute>() {

        override val path = "welcome"

        override fun create(bundle: Bundle?) = WelcomeRoute()
    }
}
