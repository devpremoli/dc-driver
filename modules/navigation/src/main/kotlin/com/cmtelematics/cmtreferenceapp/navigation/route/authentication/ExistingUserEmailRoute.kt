package com.cmtelematics.cmtreferenceapp.navigation.route.authentication

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class ExistingUserEmailRoute : Route(this) {

    companion object : Factory<ExistingUserEmailRoute>() {

        override val path = "existingUserEmail"

        override fun create(bundle: Bundle?) = ExistingUserEmailRoute()
    }
}
