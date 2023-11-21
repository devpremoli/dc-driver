package com.cmtelematics.cmtreferenceapp.navigation.route.tags

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class TagLinkedRoute : Route(this) {

    companion object : Factory<TagLinkedRoute>() {

        override val path = "tagLinked"

        override fun create(bundle: Bundle?) = TagLinkedRoute()
    }
}
