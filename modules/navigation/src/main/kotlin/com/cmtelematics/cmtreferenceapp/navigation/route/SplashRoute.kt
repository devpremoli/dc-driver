package com.cmtelematics.cmtreferenceapp.navigation.route

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
data class SplashRoute(val shouldNavigateBack: Boolean = false) : Route(this) {

    companion object : Factory<SplashRoute>() {

        override val path = "SplashRoute"

        override fun create(bundle: Bundle?) = SplashRoute()
    }
}
