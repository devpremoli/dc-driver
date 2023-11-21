package com.cmtelematics.cmtreferenceapp.navigation.route

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class MainGraphRoute : Route(this) {

    companion object : Factory<MainGraphRoute>() {

        override val path = "MainGraph"

        override fun create(bundle: Bundle?) = MainGraphRoute()
    }
}
