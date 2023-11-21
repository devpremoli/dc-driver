package com.cmtelematics.cmtreferenceapp.trips

import androidx.navigation.NavGraphBuilder
import com.cmtelematics.cmtreferenceapp.common.navigation.composable
import com.cmtelematics.cmtreferenceapp.navigation.route.trips.TripDetailsRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.trips.TripListRoute
import com.cmtelematics.cmtreferenceapp.trips.ui.TripDetailsScreen
import com.cmtelematics.cmtreferenceapp.trips.ui.TripListScreen

fun NavGraphBuilder.tripsTab() {
    composable(TripListRoute) { TripListScreen() }
}

fun NavGraphBuilder.tripListNavigation() {
    composable(TripDetailsRoute) { TripDetailsScreen() }
}
