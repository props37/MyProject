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
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.profile.ui.ProfileFeature
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.presentation.app.AppStartFeature
import ru.livetyping.zarina.presentation.feature.Features
import ru.livetyping.zarina.presentation.feature.find
import ru.livetyping.zarina.presentation.navigation.feature.cartFeature
import ru.livetyping.zarina.presentation.navigation.feature.catalogFeature
import ru.livetyping.zarina.presentation.navigation.feature.citySelectorFeature
import ru.livetyping.zarina.presentation.navigation.feature.homeFeature
import ru.livetyping.zarina.presentation.navigation.feature.onboardingFeature
import ru.livetyping.zarina.presentation.navigation.feature.profileFeature
import ru.livetyping.zarina.presentation.navigation.feature.rememberCartNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberCatalogNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberCitySelectorNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberHomeNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberOnboardingNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberProfileNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberSignInNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberWishlistNavActions
import ru.livetyping.zarina.presentation.navigation.feature.signInFeature
import ru.livetyping.zarina.presentation.navigation.feature.wishlistFeature

@Composable
fun ZarinaNavigation(
    features: Features,
    navController: NavHostController,
    startFeature: AppStartFeature,
    modifier: Modifier = Modifier,
) {
    @Suppress("NAME_SHADOWING")
    val navController by rememberUpdatedState(navController)

    // TODO: [Top] Refactor
    val homeFeature = features.find<HomeFeature>()
    val homeNavActions = rememberHomeNavActions(navController)

    val catalogFeature = features.find<CatalogFeature>()
    val catalogNavActions = rememberCatalogNavActions(navController)

    val wishlistFeature = features.find<WishlistFeature>()
    val wishlistNavActions = rememberWishlistNavActions(navController)

    val profileFeature = features.find<ProfileFeature>()
    val profileNavActions = rememberProfileNavActions(navController)

    val cartFeature = features.find<CartFeature>()
    val cartNavActions = rememberCartNavActions(navController)

    val onboardingFeature = features.find<OnboardingFeature>()
    val onboardingNavActions = rememberOnboardingNavActions(navController)

    val citySelectorFeature = features.find<CitySelectorFeature>()
    val citySelectorNavActions = rememberCitySelectorNavActions(navController)

    val signInFeature = features.find<SignInFeature>()
    val signInNavActions = rememberSignInNavActions(navController)

    val startDestination = when (startFeature) {
        AppStartFeature.ONBOARDING -> OnboardingFeature.getNavEntry()
        AppStartFeature.HOME -> HomeFeature.getNavEntry()
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { zarinaEnterSlideTransition() },
        exitTransition = { zarinaExitSlideTransition() },
        popEnterTransition = { zarinaPopEnterSlideTransition() },
        popExitTransition = { zarinaPopExitSlideTransition() },
        modifier = modifier,
    ) {
        catalogFeature(catalogFeature, catalogNavActions)
        wishlistFeature(wishlistFeature, wishlistNavActions)
        homeFeature(homeFeature, homeNavActions)
        profileFeature(navController, profileFeature, profileNavActions)
        cartFeature(navController, cartFeature, cartNavActions)

        onboardingFeature(onboardingFeature, onboardingNavActions)
        citySelectorFeature(citySelectorFeature, citySelectorNavActions)
        signInFeature(navController, signInFeature, signInNavActions)
    }
}
