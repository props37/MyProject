package ru.livetyping.zarina.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterFadeInTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaExitFadeOutTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaExitSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaPopEnterSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaPopExitSlideTransition
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.profile.ui.ProfileFeature
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature
import ru.livetyping.zarina.feature.signup.ui.api.SignUpFeature
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.presentation.app.AppStartFeature
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItems
import ru.livetyping.zarina.presentation.bottomnavbar.toFeatureNavEntry
import ru.livetyping.zarina.presentation.feature.Features
import ru.livetyping.zarina.presentation.feature.find
import ru.livetyping.zarina.presentation.navigation.feature.cartFeature
import ru.livetyping.zarina.presentation.navigation.feature.catalogFeature
import ru.livetyping.zarina.presentation.navigation.feature.citySelectorFeature
import ru.livetyping.zarina.presentation.navigation.feature.homeFeature
import ru.livetyping.zarina.presentation.navigation.feature.onboardingFeature
import ru.livetyping.zarina.presentation.navigation.feature.productListFeature
import ru.livetyping.zarina.presentation.navigation.feature.profileFeature
import ru.livetyping.zarina.presentation.navigation.feature.rememberCartNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberCatalogNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberCitySelectorNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberHomeNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberOnboardingNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberProductListNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberProfileNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberSignInNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberSignUpNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberWishlistNavActions
import ru.livetyping.zarina.presentation.navigation.feature.signInFeature
import ru.livetyping.zarina.presentation.navigation.feature.signUpFeature
import ru.livetyping.zarina.presentation.navigation.feature.wishlistFeature
import ru.livetyping.zarina.presentation.navigation.util.initialDestination
import ru.livetyping.zarina.presentation.navigation.util.targetDestination

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

    val signUpFeature = features.find<SignUpFeature>()
    val signUpNavActions = rememberSignUpNavActions(navController)

    val productListFeature = features.find<ProductListFeature>()
    val productListNavActions = rememberProductListNavActions(navController)

    val startDestination = when (startFeature) {
        AppStartFeature.ONBOARDING -> OnboardingFeature.getNavEntry()
        AppStartFeature.HOME -> HomeFeature.getNavEntry()
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            enterTransition(
                defaultTransition = ::zarinaEnterSlideTransition,
                transitionBetweenBottomNavBarItems = ::zarinaEnterFadeInTransition,
            )
        },
        exitTransition = {
            exitTransition(
                defaultTransition = ::zarinaExitSlideTransition,
                transitionBetweenBottomNavBarItems = ::zarinaExitFadeOutTransition,
            )
        },
        popEnterTransition = {
            enterTransition(
                defaultTransition = ::zarinaPopEnterSlideTransition,
                transitionBetweenBottomNavBarItems = ::zarinaEnterFadeInTransition,
            )
        },
        popExitTransition = {
            exitTransition(
                defaultTransition = ::zarinaPopExitSlideTransition,
                transitionBetweenBottomNavBarItems = ::zarinaExitFadeOutTransition,
            )
        },
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
        signUpFeature(navController, signUpFeature, signUpNavActions)
        productListFeature(productListFeature, productListNavActions)
    }
}

private inline fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(
    defaultTransition: () -> EnterTransition,
    transitionBetweenBottomNavBarItems: () -> EnterTransition,
): EnterTransition {
    val initialDestinationBottomNavBarItem = initialDestination.findClosestBottomNavBarItem()
    val targetDestinationBottomNavBarItem = targetDestination.findClosestBottomNavBarItem()
    return when {
        initialDestinationBottomNavBarItem == null || targetDestinationBottomNavBarItem == null -> {
            defaultTransition()
        }

        initialDestinationBottomNavBarItem == targetDestinationBottomNavBarItem -> {
            defaultTransition()
        }

        else -> transitionBetweenBottomNavBarItems()
    }
}

private inline fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(
    defaultTransition: () -> ExitTransition,
    transitionBetweenBottomNavBarItems: () -> ExitTransition,
): ExitTransition {
    val initialDestinationBottomNavBarItem = initialDestination.findClosestBottomNavBarItem()
    val targetDestinationBottomNavBarItem = targetDestination.findClosestBottomNavBarItem()
    return when {
        initialDestinationBottomNavBarItem == null || targetDestinationBottomNavBarItem == null -> {
            defaultTransition()
        }

        initialDestinationBottomNavBarItem == targetDestinationBottomNavBarItem -> {
            defaultTransition()
        }

        else -> transitionBetweenBottomNavBarItems()
    }
}

private fun NavDestination.findClosestBottomNavBarItem(): BottomNavBarItem? {
    return this.hierarchy.firstNotNullOfOrNull { destination ->
        var resultItem: BottomNavBarItem? = null
        for (item in BottomNavBarItems) {
            if (destination.hasRoute(item.toFeatureNavEntry()::class)) {
                resultItem = item
                break
            }
        }
        resultItem
    }
}
