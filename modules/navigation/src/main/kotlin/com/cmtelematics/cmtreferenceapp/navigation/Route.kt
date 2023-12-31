package com.cmtelematics.cmtreferenceapp.navigation

import android.os.Bundle
import android.os.Parcelable
import com.cmtelematics.cmtreferenceapp.navigation.Constants.ARG_ROUTE

abstract class Route(val factory: Factory<*>) : Parcelable {

    fun toPath() = factory.path

    open fun toBundle(): Bundle =
        Bundle(1).apply { putParcelable(ARG_ROUTE, this@Route) }

    open class Factory<T : Route> {

        open val path: String
            get() = error("You must specify the path for the route!")

        @Suppress("Deprecation") // TODO https://issuetracker.google.com/issues/242048899
        open fun create(bundle: Bundle?): T =
            requireNotNull(bundle?.getParcelable(ARG_ROUTE)) { "Route was not set." }

        companion object {

            inline fun <reified F : Factory<*>> create(): F = F::class.java.newInstance()
        }
    }
}
