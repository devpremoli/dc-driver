package com.cmtelematics.cmtreferenceapp.navigation.route.crash

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class CrashAssistRoute : Route(this) {

    companion object : Factory<CrashAssistRoute>() {
        override val path = "crashAssist"
        override fun create(bundle: Bundle?) = CrashAssistRoute()
    }
}
