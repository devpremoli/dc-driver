package com.cmtelematics.cmtreferenceapp.navigation.route.settings

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class DebugMenuRoute : Route(this) {

    companion object : Factory<DebugMenuRoute>() {

        override val path = "debugMenu"

        override fun create(bundle: Bundle?) = DebugMenuRoute()
    }
}
