package com.cmtelematics.cmtreferenceapp.navigation.route.settings

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class SettingsRoute : Route(this) {

    companion object : Factory<SettingsRoute>() {

        override val path = "settings"

        override fun create(bundle: Bundle?) = SettingsRoute()
    }
}
