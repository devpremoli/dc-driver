package com.cmtelematics.cmtreferenceapp.authentication

import androidx.navigation.NavGraphBuilder
import com.cmtelematics.cmtreferenceapp.authentication.ui.ExistingUserEmailScreen
import com.cmtelematics.cmtreferenceapp.authentication.ui.RegisterEmailScreen
import com.cmtelematics.cmtreferenceapp.authentication.ui.VerifyCodeScreen
import com.cmtelematics.cmtreferenceapp.common.navigation.composable
import com.cmtelematics.cmtreferenceapp.navigation.route.authentication.ExistingUserEmailRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.authentication.RegisterEmailRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.authentication.VerifyCodeRoute

fun NavGraphBuilder.authenticationNavigation() {
    composable(RegisterEmailRoute) { RegisterEmailScreen() }
    composable(ExistingUserEmailRoute) { ExistingUserEmailScreen() }
    composable(VerifyCodeRoute) { VerifyCodeScreen() }
}
