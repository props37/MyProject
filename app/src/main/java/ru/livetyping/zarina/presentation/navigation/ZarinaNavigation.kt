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
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavEntry
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.presentation.feature.Features
import ru.livetyping.zarina.presentation.feature.find
import ru.livetyping.zarina.presentation.navigation.base.Destination
import ru.livetyping.zarina.presentation.navigation.feature.catalogFeature
import ru.livetyping.zarina.presentation.navigation.feature.citySelectorFeature
import ru.livetyping.zarina.presentation.navigation.feature.homeFeature
import ru.livetyping.zarina.presentation.navigation.feature.onboardingFeature
import ru.livetyping.zarina.presentation.navigation.feature.rememberCatalogNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberCitySelectorNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberHomeNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberOnboardingNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberWishlistNavActions
import ru.livetyping.zarina.presentation.navigation.feature.wishlistFeature

@Composable
fun ZarinaNavigation(
    features: Features,
    navController: NavHostController,
    startDestination: Destination<Unit>,
    modifier: Modifier = Modifier,
) {
    @Suppress("NAME_SHADOWING")
    val navController by rememberUpdatedState(navController)

    // TODO: [Top] Refactor
    val onboardingFeature = features.find<OnboardingFeature>()
    val onboardingNavActions = rememberOnboardingNavActions(features, navController)

    val citySelectorFeature = features.find<CitySelectorFeature>()
    val citySelectorNavActions = rememberCitySelectorNavActions(features, navController)

    val homeFeature = features.find<HomeFeature>()
    val homeNavActions = rememberHomeNavActions(features, navController)

    val catalogFeature = features.find<CatalogFeature>()
    val catalogNavActions = rememberCatalogNavActions(features, navController)

    val wishlistFeature = features.find<WishlistFeature>()
    val wishlistNavActions = rememberWishlistNavActions(features, navController)

    NavHost(
        navController = navController,
        startDestination = OnboardingNavEntry, // TODO: [Top] Implement
        enterTransition = { zarinaEnterSlideTransition() },
        exitTransition = { zarinaExitSlideTransition() },
        popEnterTransition = { zarinaPopEnterSlideTransition() },
        popExitTransition = { zarinaPopExitSlideTransition() },
        modifier = modifier,
    ) {
        onboardingFeature(onboardingFeature, onboardingNavActions, features)
        homeFeature(homeFeature, homeNavActions, features)
        catalogFeature(catalogFeature, catalogNavActions)
        wishlistFeature(wishlistFeature, wishlistNavActions)
        citySelectorFeature(citySelectorFeature, citySelectorNavActions, features)
    }
}
