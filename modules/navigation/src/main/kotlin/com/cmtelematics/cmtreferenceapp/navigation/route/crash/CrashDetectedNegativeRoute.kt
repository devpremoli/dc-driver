package com.cmtelematics.cmtreferenceapp.navigation.route.crash

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class CrashDetectedNegativeRoute : Route(this) {

    companion object : Factory<CrashDetectedNegativeRoute>() {
        override val path = "crashDetectedNegative"
        override fun create(bundle: Bundle?) = CrashDetectedNegativeRoute()
    }
}
