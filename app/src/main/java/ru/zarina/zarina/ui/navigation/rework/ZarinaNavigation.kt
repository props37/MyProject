package ru.zarina.zarina.ui.navigation.rework

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.rework.destination.cartGraph
import ru.zarina.zarina.ui.navigation.rework.destination.catalogGraph
import ru.zarina.zarina.ui.navigation.rework.destination.citySelectorBottomSheetScreen
import ru.zarina.zarina.ui.navigation.rework.destination.defaultCityDialogScreen
import ru.zarina.zarina.ui.navigation.rework.destination.favoritesGraph
import ru.zarina.zarina.ui.navigation.rework.destination.homeGraph
import ru.zarina.zarina.ui.navigation.rework.destination.onboardingScreen
import ru.zarina.zarina.ui.navigation.rework.destination.productsScreen
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
        enterTransition = { fadeIn(tween(TransitionDurationMillis)) },
        exitTransition = { fadeOut(tween(TransitionDurationMillis)) },
        modifier = modifier,
    ) {
        // Bottom nav bar graphs
        catalogGraph(navController)
        favoritesGraph(navController)
        homeGraph(navController)
        profileGraph(navController)
        cartGraph(navController)

        onboardingScreen(navController)
        citySelectorBottomSheetScreen(navController)
        defaultCityDialogScreen(navController)
        productsScreen(navController)
    }
}

private const val TransitionDurationMillis = 300
