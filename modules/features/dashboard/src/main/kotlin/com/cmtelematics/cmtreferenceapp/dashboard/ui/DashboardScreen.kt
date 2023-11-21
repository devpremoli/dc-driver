package com.cmtelematics.cmtreferenceapp.dashboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cmtelematics.cmtreferenceapp.common.navigation.NavHost
import com.cmtelematics.cmtreferenceapp.dashboard.R
import com.cmtelematics.cmtreferenceapp.dashboard.dashboardTabsNavigation
import com.cmtelematics.cmtreferenceapp.dashboard.model.BottomNavigationRoute
import com.cmtelematics.cmtreferenceapp.dashboard.model.DashboardViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.cmtelematics.cmtreferenceapp.navigation.route.dashboard.DashboardGraphRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.dashboard.HomeRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.settings.SettingsRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.trips.TripListRoute
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    DashboardScreenContent(viewModel.initialBottomNavigationRoute) { viewModel.logTabScreenNavigation(it) }
}

@Composable
private fun DashboardScreenContent(initialTabRoute: Route.Factory<*>, onTabClicked: (BottomNavigationRoute) -> Unit) {
    val navController = rememberNavController()

    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            navController = navController,
            startDestination = initialTabRoute,
            routeFactory = DashboardGraphRoute
        ) {
            dashboardTabsNavigation()
        }

        TabBar(navController, onTabClicked)
    }
}

@Composable
private fun TabBar(navController: NavHostController, onTabClicked: (BottomNavigationRoute) -> Unit) {
    val items = remember { getBottomNavigationItems() }
    BottomNavigation(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = AppTheme.colors.background.primary
    ) {
        TabItems(items, navController, onTabClicked)
    }
}

@Composable
private fun RowScope.TabItems(
    items: List<BottomNavigationRoute>,
    navController: NavHostController,
    onTabClicked: (BottomNavigationRoute) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.run { destination.route }

    items.forEach { bottomNavigationRoute ->
        val isSelected = currentRoute == bottomNavigationRoute.routeFactory.path
        BottomNavigationItem(
            selected = isSelected,
            label = { TabLabel(bottomNavigationRoute, isSelected) },
            icon = { TabIcon(bottomNavigationRoute) },
            selectedContentColor = AppTheme.colors.tabBar.selectedColor,
            unselectedContentColor = AppTheme.colors.tabBar.unselectedColor,
            onClick = {
                onTabClicked(bottomNavigationRoute)
                handleTabClick(navController, bottomNavigationRoute)
            }
        )
    }
}

@Composable
private fun TabLabel(
    bottomNavigationRoute: BottomNavigationRoute,
    isSelected: Boolean
) {
    Text(
        text = stringResource(bottomNavigationRoute.labelRes),
        style = AppTheme.typography.button.tab.style,
        color = if (isSelected) {
            AppTheme.colors.tabBar.selectedColor
        } else {
            AppTheme.colors.tabBar.unselectedColor
        }
    )
}

@Composable
private fun TabIcon(bottomNavigationRoute: BottomNavigationRoute) {
    Icon(
        painter = painterResource(bottomNavigationRoute.iconRes),
        contentDescription = stringResource(bottomNavigationRoute.labelRes)
    )
}

private fun handleTabClick(
    navController: NavHostController,
    bottomNavigationRoute: BottomNavigationRoute
) {
    navController.navigate(bottomNavigationRoute.routeFactory.path) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun getBottomNavigationItems() = listOf(
    BottomNavigationRoute(HomeRoute, R.drawable.ic_tab_home, R.string.title_home_tab),
    BottomNavigationRoute(TripListRoute, R.drawable.ic_tab_trips, R.string.title_trip_list),
    BottomNavigationRoute(SettingsRoute, R.drawable.ic_tab_settings, R.string.title_settings)
)

@Preview
@Composable
private fun VerifyCodeScreenPreview() {
    AppTheme {
        DashboardScreenContent(HomeRoute) { }
    }
}
