package com.cmtelematics.cmtreferenceapp.navigation.route.authentication

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class RegisterEmailRoute : Route(this) {

    companion object : Factory<RegisterEmailRoute>() {

        override val path = "registerEmail"

        override fun create(bundle: Bundle?) = RegisterEmailRoute()
    }
}
