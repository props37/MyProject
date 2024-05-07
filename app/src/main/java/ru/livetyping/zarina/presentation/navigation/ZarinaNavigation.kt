package ru.livetyping.zarina.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.livetyping.zarina.presentation.navigation.base.Destination
import ru.livetyping.zarina.presentation.navigation.screen.citySelectorBottomSheetScreen
import ru.livetyping.zarina.presentation.navigation.screen.defaultCityDialog
import ru.livetyping.zarina.presentation.navigation.screen.filtersScreen
import ru.livetyping.zarina.presentation.navigation.screen.graph.cartGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.catalogGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.favoritesGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.homeGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.profileGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.signInGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.signUpGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.sizeSelectorGraph
import ru.livetyping.zarina.presentation.navigation.screen.listFilterScreen
import ru.livetyping.zarina.presentation.navigation.screen.onboardingScreen
import ru.livetyping.zarina.presentation.navigation.screen.productScreen
import ru.livetyping.zarina.presentation.navigation.screen.productSubscriptionScreen
import ru.livetyping.zarina.presentation.navigation.screen.productsScreen
import ru.livetyping.zarina.presentation.navigation.util.NavigationTransitionDurationMillis

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

        signUpGraph(updatedNavController)
        signInGraph(updatedNavController)
        sizeSelectorGraph(updatedNavController)

        onboardingScreen(updatedNavController)
        citySelectorBottomSheetScreen(updatedNavController)
        defaultCityDialog(updatedNavController)
        productsScreen(updatedNavController)
        productScreen(updatedNavController)
        filtersScreen(updatedNavController)
        listFilterScreen(updatedNavController)
        productSubscriptionScreen(updatedNavController)
    }
}
