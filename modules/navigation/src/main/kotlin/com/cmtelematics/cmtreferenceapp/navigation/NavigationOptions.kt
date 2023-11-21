package com.cmtelematics.cmtreferenceapp.navigation

data class NavigationOptions(
    val popUpTo: Route.Factory<*>,
    val popUpToInclusive: Boolean
)
