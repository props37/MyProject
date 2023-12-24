package ru.zarina.zarina.ui.navigation.rework

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.rework.destination.cartGraph
import ru.zarina.zarina.ui.navigation.rework.destination.catalogGraph
import ru.zarina.zarina.ui.navigation.rework.destination.citySelectorBottomSheetScreen
import ru.zarina.zarina.ui.navigation.rework.destination.favoritesGraph
import ru.zarina.zarina.ui.navigation.rework.destination.homeGraph
import ru.zarina.zarina.ui.navigation.rework.destination.onboardingScreen
import ru.zarina.zarina.ui.navigation.rework.destination.profileGraph

@Composable
fun ZarinaNavigation(
    navController: NavHostController,
    startDestination: Destination<Unit>,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.routeSchema,
        modifier = modifier,
    ) {
        catalogGraph(navController)
        favoritesGraph(navController)
        homeGraph(navController)
        profileGraph(navController)
        cartGraph(navController)

        onboardingScreen(navController)
        citySelectorBottomSheetScreen(navController)
    }
}
