package ru.zarina.zarina.ui.navigation.rework

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
import ru.zarina.zarina.ui.navigation.rework.screen.cartGraph
import ru.zarina.zarina.ui.navigation.rework.screen.catalogGraph
import ru.zarina.zarina.ui.navigation.rework.screen.citySelectorBottomSheetScreen
import ru.zarina.zarina.ui.navigation.rework.screen.defaultCityDialogScreen
import ru.zarina.zarina.ui.navigation.rework.screen.favoritesGraph
import ru.zarina.zarina.ui.navigation.rework.screen.homeGraph
import ru.zarina.zarina.ui.navigation.rework.screen.onboardingScreen
import ru.zarina.zarina.ui.navigation.rework.screen.profileGraph
import ru.zarina.zarina.ui.navigation.rework.screen.sizeSelectorGraph
import ru.zarina.zarina.ui.navigation.rework.screen.unscoped.filtersScreen
import ru.zarina.zarina.ui.navigation.rework.screen.unscoped.listFilterScreen
import ru.zarina.zarina.ui.navigation.rework.screen.unscoped.productSubscriptionScreen
import ru.zarina.zarina.ui.navigation.rework.screen.unscoped.productsScreen

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

const val NavigationTransitionDurationMillis = 300
