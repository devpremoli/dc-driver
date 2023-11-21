package com.cmtelematics.cmtreferenceapp.navigation.route.crash

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class AmbulanceCancelledRoute : Route(this) {

    companion object : Factory<AmbulanceCancelledRoute>() {
        override val path = "ambulanceCancelledRoute"
        override fun create(bundle: Bundle?) = AmbulanceCancelledRoute()
    }
}
