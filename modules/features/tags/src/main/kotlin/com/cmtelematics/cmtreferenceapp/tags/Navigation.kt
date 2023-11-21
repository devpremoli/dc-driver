package com.cmtelematics.cmtreferenceapp.tags

import androidx.navigation.NavGraphBuilder
import com.cmtelematics.cmtreferenceapp.common.navigation.composable
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.CreateVehicleRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.LinkTagRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.TagLinkedRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.VehicleDetailsRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.VehicleListRoute
import com.cmtelematics.cmtreferenceapp.tags.ui.CreateVehicleScreen
import com.cmtelematics.cmtreferenceapp.tags.ui.LinkTagScreen
import com.cmtelematics.cmtreferenceapp.tags.ui.TagLinkedScreen
import com.cmtelematics.cmtreferenceapp.tags.ui.VehicleDetailsScreen
import com.cmtelematics.cmtreferenceapp.tags.ui.VehicleListScreen

fun NavGraphBuilder.tagsNavigation() {
    composable(VehicleListRoute) { VehicleListScreen() }
    composable(CreateVehicleRoute) { CreateVehicleScreen() }
    composable(LinkTagRoute) { LinkTagScreen() }
    composable(TagLinkedRoute) { TagLinkedScreen() }
    composable(VehicleDetailsRoute) { VehicleDetailsScreen() }
}
