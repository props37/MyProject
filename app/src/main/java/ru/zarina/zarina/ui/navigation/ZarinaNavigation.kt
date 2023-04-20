package ru.zarina.zarina.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.ui.screens.cityselection.CitySelectionScreen
import ru.zarina.zarina.ui.screens.onboarding.OnboardingScreen

@Composable
fun ZarinaNavigation(
    startDestination: Destination<*>,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination.routeSchema,
    ) {
        composableDestination(Destinations.HOME) {
            Box(modifier = Modifier.fillMaxSize()) {}
        }
        composableDestination(Destinations.ONBOARDING) {
            OnboardingScreen(
                showHome = {
                    navController.navigate(Destinations.HOME.route) { popUpTo(0) }
                },
                showCitySelection = {
                    navController.navigate(Destinations.CITY_SELECTION.route)
                }
            )
        }
        composableDestination(Destinations.CITY_SELECTION) {
            CitySelectionScreen(
                showHome = {
                    navController.navigate(Destinations.HOME.route) { popUpTo(0) }
                }
            )
        }
    }
}
