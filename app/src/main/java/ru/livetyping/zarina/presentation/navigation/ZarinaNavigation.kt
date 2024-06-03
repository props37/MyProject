package ru.livetyping.zarina.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.livetyping.zarina.presentation.navigation.base.Destination
import ru.livetyping.zarina.presentation.navigation.screen.citySelectorScreen
import ru.livetyping.zarina.presentation.navigation.screen.defaultCityDialog
import ru.livetyping.zarina.presentation.navigation.screen.genericBottomSheetScreen
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
import ru.livetyping.zarina.presentation.navigation.screen.permissionRequirementBottomSheetScreen
import ru.livetyping.zarina.presentation.navigation.screen.productFiltersScreen
import ru.livetyping.zarina.presentation.navigation.screen.productScreen
import ru.livetyping.zarina.presentation.navigation.screen.productSearchFiltersScreen
import ru.livetyping.zarina.presentation.navigation.screen.productSearchScreen
import ru.livetyping.zarina.presentation.navigation.screen.productSubscriptionScreen
import ru.livetyping.zarina.presentation.navigation.screen.productsScreen
import ru.livetyping.zarina.presentation.navigation.util.fadeInTransition
import ru.livetyping.zarina.presentation.navigation.util.fadeOutTransition

@Composable
fun ZarinaNavigation(
    navController: NavHostController,
    startDestination: Destination<Unit>,
    modifier: Modifier = Modifier,
) {
    @Suppress("NAME_SHADOWING")
    val navController by rememberUpdatedState(navController)

    NavHost(
        navController = navController,
        startDestination = startDestination.routeSchema,
        enterTransition = { fadeInTransition() },
        exitTransition = { fadeOutTransition() },
        modifier = modifier,
    ) {
        // Bottom nav bar graphs
        catalogGraph(navController)
        favoritesGraph(navController)
        homeGraph(navController)
        profileGraph(navController)
        cartGraph(navController)

        signUpGraph(navController)
        signInGraph(navController)
        sizeSelectorGraph(navController)

        onboardingScreen(navController)
        citySelectorScreen(navController)
        defaultCityDialog(navController)
        productsScreen(navController)
        productFiltersScreen(navController)
        productSearchScreen(navController)
        productSearchFiltersScreen(navController)
        productScreen(navController)
        listFilterScreen(navController)
        productSubscriptionScreen(navController)
        permissionRequirementBottomSheetScreen(navController)

        genericBottomSheetScreen(navController)
    }
}
