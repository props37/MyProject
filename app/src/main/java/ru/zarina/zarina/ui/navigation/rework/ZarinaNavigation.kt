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
import ru.zarina.zarina.ui.navigation.rework.destination.cartGraph
import ru.zarina.zarina.ui.navigation.rework.destination.catalogGraph
import ru.zarina.zarina.ui.navigation.rework.destination.citySelectorBottomSheetScreen
import ru.zarina.zarina.ui.navigation.rework.destination.defaultCityDialogScreen
import ru.zarina.zarina.ui.navigation.rework.destination.favoritesGraph
import ru.zarina.zarina.ui.navigation.rework.destination.homeGraph
import ru.zarina.zarina.ui.navigation.rework.destination.onboardingScreen
import ru.zarina.zarina.ui.navigation.rework.destination.profileGraph
import ru.zarina.zarina.ui.navigation.rework.destination.unscoped.filtersScreen
import ru.zarina.zarina.ui.navigation.rework.destination.unscoped.heightSelectorBottomSheetScreen
import ru.zarina.zarina.ui.navigation.rework.destination.unscoped.listFilterScreen
import ru.zarina.zarina.ui.navigation.rework.destination.unscoped.productSubscriptionScreen
import ru.zarina.zarina.ui.navigation.rework.destination.unscoped.productsScreen
import ru.zarina.zarina.ui.navigation.rework.destination.unscoped.sizeSelectorBottomSheetScreen

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

        onboardingScreen(updatedNavController)
        citySelectorBottomSheetScreen(updatedNavController)
        defaultCityDialogScreen(updatedNavController)
        productsScreen(updatedNavController)
        filtersScreen(updatedNavController)
        listFilterScreen(updatedNavController)
        sizeSelectorBottomSheetScreen(updatedNavController)
        heightSelectorBottomSheetScreen(updatedNavController)
        productSubscriptionScreen(updatedNavController)
    }
}

const val NavigationTransitionDurationMillis = 300
