package com.cmtelematics.cmtreferenceapp.navigation.route.authentication

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class VerifyCodeRoute(val email: String) : Route(this) {

    override fun toBundle(): Bundle = super.toBundle().apply {
        putString(ARG_EMAIL, email)
    }

    companion object : Factory<VerifyCodeRoute>() {

        private const val ARG_EMAIL = "EMAIL"

        override val path = "verifyCode"

        override fun create(bundle: Bundle?): VerifyCodeRoute =
            VerifyCodeRoute(requireNotNull(bundle?.getString(ARG_EMAIL)) { "EMAIL was not set" })
    }
}
