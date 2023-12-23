package ru.zarina.zarina.ui.navigation.rework

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.zarina.zarina.ui.navigation.rework.destination.cartGraph
import ru.zarina.zarina.ui.navigation.rework.destination.catalogGraph
import ru.zarina.zarina.ui.navigation.rework.destination.favoritesGraph
import ru.zarina.zarina.ui.navigation.rework.destination.homeGraph
import ru.zarina.zarina.ui.navigation.rework.destination.onboardingScreen
import ru.zarina.zarina.ui.navigation.rework.destination.profileGraph
import ru.zarina.zarina.ui.navigation.rework.graph.CommonDestinations

@Composable
fun ZarinaNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = CommonDestinations.Onboarding.route,
        modifier = modifier,
    ) {
        catalogGraph(navController)
        favoritesGraph(navController)
        homeGraph(navController)
        profileGraph(navController)
        cartGraph(navController)

        onboardingScreen(navController)
    }
}
