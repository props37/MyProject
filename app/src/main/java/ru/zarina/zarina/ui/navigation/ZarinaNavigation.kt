package ru.zarina.zarina.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.screen.citySelectorBottomSheetScreen
import ru.zarina.zarina.ui.navigation.screen.defaultCityDialogScreen
import ru.zarina.zarina.ui.navigation.screen.filtersScreen
import ru.zarina.zarina.ui.navigation.screen.graph.cartGraph
import ru.zarina.zarina.ui.navigation.screen.graph.catalogGraph
import ru.zarina.zarina.ui.navigation.screen.graph.favoritesGraph
import ru.zarina.zarina.ui.navigation.screen.graph.homeGraph
import ru.zarina.zarina.ui.navigation.screen.graph.profileGraph
import ru.zarina.zarina.ui.navigation.screen.graph.sizeSelectorGraph
import ru.zarina.zarina.ui.navigation.screen.listFilterScreen
import ru.zarina.zarina.ui.navigation.screen.onboardingScreen
import ru.zarina.zarina.ui.navigation.screen.productSubscriptionScreen
import ru.zarina.zarina.ui.navigation.screen.productsScreen
import ru.zarina.zarina.ui.navigation.util.NavigationTransitionDurationMillis

@Composable
fun ZarinaNavigation(
    navController: NavHostController,
    startDestination: Destination<Unit>,
    modifier: Modifier = Modifier,
) {
    val updatedNavController by rememberUpdatedState(navController)

    NavHost(
        navController = navController,
        startDestination = startDestination.routeSchema,
        enterTransition = { fadeIn(tween(NavigationTransitionDurationMillis)) },
        exitTransition = { fadeOut(tween(NavigationTransitionDurationMillis)) },
        modifier = modifier,
    ) {
        // Bottom nav bar graphs
        catalogGraph(updatedNavController)
        favoritesGraph(updatedNavController)
        homeGraph(updatedNavController)
        profileGraph(updatedNavController)
        cartGraph(updatedNavController)

        sizeSelectorGraph(updatedNavController)

        onboardingScreen(updatedNavController)
        citySelectorBottomSheetScreen(updatedNavController)
        defaultCityDialogScreen(updatedNavController)
        productsScreen(updatedNavController)
        filtersScreen(updatedNavController)
        listFilterScreen(updatedNavController)
        productSubscriptionScreen(updatedNavController)
    }
}
