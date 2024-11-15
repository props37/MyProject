package ru.livetyping.zarina.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaExitSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaPopEnterSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaPopExitSlideTransition
import ru.livetyping.zarina.presentation.feature.Features
import ru.livetyping.zarina.presentation.navigation.base.Destination
import ru.livetyping.zarina.presentation.navigation.screen.citySelectorScreen
import ru.livetyping.zarina.presentation.navigation.screen.defaultCityDialog
import ru.livetyping.zarina.presentation.navigation.screen.genericBottomSheetScreen
import ru.livetyping.zarina.presentation.navigation.screen.graph.cartGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.catalogGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.checkoutGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.favoritesGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.homeGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.loyaltyProgramGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.profileGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.signInGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.signUpGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.sizeSelectorGraph
import ru.livetyping.zarina.presentation.navigation.screen.listFilterScreen
import ru.livetyping.zarina.presentation.navigation.screen.onboardingScreen
import ru.livetyping.zarina.presentation.navigation.screen.paymentScreen
import ru.livetyping.zarina.presentation.navigation.screen.permissionRequirementBottomSheetScreen
import ru.livetyping.zarina.presentation.navigation.screen.productFiltersScreen
import ru.livetyping.zarina.presentation.navigation.screen.productScreen
import ru.livetyping.zarina.presentation.navigation.screen.productSearchFiltersScreen
import ru.livetyping.zarina.presentation.navigation.screen.productSearchScreen
import ru.livetyping.zarina.presentation.navigation.screen.productSubscriptionScreen
import ru.livetyping.zarina.presentation.navigation.screen.productsScreen

@Composable
fun ZarinaNavigation(
    features: Features,
    navController: NavHostController,
    startDestination: Destination<Unit>,
    modifier: Modifier = Modifier,
) {
    @Suppress("NAME_SHADOWING")
    val navController by rememberUpdatedState(navController)

    NavHost(
        navController = navController,
        startDestination = startDestination.routeSchema,
        enterTransition = { zarinaEnterSlideTransition() },
        exitTransition = { zarinaExitSlideTransition() },
        popEnterTransition = { zarinaPopEnterSlideTransition() },
        popExitTransition = { zarinaPopExitSlideTransition() },
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
        loyaltyProgramGraph(navController)
        checkoutGraph(navController)

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
        paymentScreen(navController)

        genericBottomSheetScreen(navController)
    }
}
