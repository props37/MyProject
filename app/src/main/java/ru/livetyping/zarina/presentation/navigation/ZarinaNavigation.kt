package ru.livetyping.zarina.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
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
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityFeature
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature
import ru.livetyping.zarina.feature.profile.ui.ProfileFeature
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature
import ru.livetyping.zarina.feature.signup.ui.api.SignUpFeature
import ru.livetyping.zarina.feature.webview.ui.WebViewFeature
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
import ru.livetyping.zarina.presentation.navigation.feature.detectedCityFeature
import ru.livetyping.zarina.presentation.navigation.feature.homeFeature
import ru.livetyping.zarina.presentation.navigation.feature.onboardingFeature
import ru.livetyping.zarina.presentation.navigation.feature.productFeature
import ru.livetyping.zarina.presentation.navigation.feature.productListFeature
import ru.livetyping.zarina.presentation.navigation.feature.productSubscriptionFeature
import ru.livetyping.zarina.presentation.navigation.feature.profileFeature
import ru.livetyping.zarina.presentation.navigation.feature.rememberCartNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberCartNavResultRetrievers
import ru.livetyping.zarina.presentation.navigation.feature.rememberCatalogNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberCitySelectorNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberDetectedCityNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberHomeNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberOnboardingNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberOnboardingNavResultRetrievers
import ru.livetyping.zarina.presentation.navigation.feature.rememberProductListNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberProductNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberProductSubscriptionNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberProfileNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberProfileNavResultRetrievers
import ru.livetyping.zarina.presentation.navigation.feature.rememberSearchNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberSignInNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberWebViewNavActions
import ru.livetyping.zarina.presentation.navigation.feature.rememberWishlistNavActions
import ru.livetyping.zarina.presentation.navigation.feature.searchFeature
import ru.livetyping.zarina.presentation.navigation.feature.signInFeature
import ru.livetyping.zarina.presentation.navigation.feature.signUpFeature
import ru.livetyping.zarina.presentation.navigation.feature.webViewFeature
import ru.livetyping.zarina.presentation.navigation.feature.wishlistFeature
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
    val profileNavResultRetrievers = rememberProfileNavResultRetrievers()

    val cartFeature = features.find<CartFeature>()
    val cartNavActions = rememberCartNavActions(navController)
    val cartNavResultRetrievers = rememberCartNavResultRetrievers()

    val onboardingFeature = features.find<OnboardingFeature>()
    val onboardingNavActions = rememberOnboardingNavActions(navController)
    val onboardingNavResultRetrievers = rememberOnboardingNavResultRetrievers()

    val citySelectorFeature = features.find<CitySelectorFeature>()
    val citySelectorNavActions = rememberCitySelectorNavActions(navController)

    val signInFeature = features.find<SignInFeature>()
    val signInNavActions = rememberSignInNavActions(navController)

    val signUpFeature = features.find<SignUpFeature>()

    val productListFeature = features.find<ProductListFeature>()
    val productListNavActions = rememberProductListNavActions(navController)

    val productFeature = features.find<ProductFeature>()
    val productNavActions = rememberProductNavActions(navController)

    val productSubscriptionFeature = features.find<ProductSubscriptionFeature>()
    val productSubscriptionNavActions = rememberProductSubscriptionNavActions(navController)

    val detectedCityFeature = features.find<DetectedCityFeature>()
    val detectedCityNavActions = rememberDetectedCityNavActions(navController)

    val webViewFeature = features.find<WebViewFeature>()
    val webViewNavActions = rememberWebViewNavActions(navController)

    val searchFeature = features.find<SearchFeature>()
    val searchNavActions = rememberSearchNavActions(navController)

    val startDestination = when (startFeature) {
        AppStartFeature.ONBOARDING -> OnboardingFeature.NavEntry
        AppStartFeature.HOME -> HomeFeature.NavEntry
    }

    val currentBackStack = navController.currentBackStack
    var prevDestination by remember { mutableStateOf<NavDestination?>(null) }
    var currentDestination by remember { mutableStateOf<NavDestination?>(null) }
    var prevSelectedBottomNavBarItem by remember { mutableStateOf<BottomNavBarItem?>(null) }
    DisposableEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination != currentDestination) {
                prevDestination = currentDestination
                currentDestination = destination

                val backStack = currentBackStack.value
                prevSelectedBottomNavBarItem =
                    prevDestination?.findClosestBottomNavBarItem(backStack)
            }
        }
        onDispose {}
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            enterTransition(
                backStack = currentBackStack.value,
                lastSelectedBottomNavBarItem = prevSelectedBottomNavBarItem,
                defaultTransition = ::zarinaEnterSlideTransition,
                transitionBetweenBottomNavBarItems = ::zarinaEnterFadeInTransition,
            )
        },
        exitTransition = {
            exitTransition(
                backStack = currentBackStack.value,
                lastSelectedBottomNavBarItem = prevSelectedBottomNavBarItem,
                defaultTransition = ::zarinaExitSlideTransition,
                transitionBetweenBottomNavBarItems = ::zarinaExitFadeOutTransition,
            )
        },
        popEnterTransition = {
            enterTransition(
                backStack = currentBackStack.value,
                lastSelectedBottomNavBarItem = prevSelectedBottomNavBarItem,
                defaultTransition = ::zarinaPopEnterSlideTransition,
                transitionBetweenBottomNavBarItems = ::zarinaEnterFadeInTransition,
            )
        },
        popExitTransition = {
            exitTransition(
                backStack = currentBackStack.value,
                lastSelectedBottomNavBarItem = prevSelectedBottomNavBarItem,
                defaultTransition = ::zarinaPopExitSlideTransition,
                transitionBetweenBottomNavBarItems = ::zarinaExitFadeOutTransition,
            )
        },
        modifier = modifier,
    ) {
        catalogFeature(navController, catalogFeature, catalogNavActions)
        wishlistFeature(navController, wishlistFeature, wishlistNavActions)
        homeFeature(navController, homeFeature, homeNavActions)
        profileFeature(navController, profileFeature, profileNavActions, profileNavResultRetrievers)
        cartFeature(navController, cartFeature, cartNavActions, cartNavResultRetrievers)

        onboardingFeature(onboardingFeature, onboardingNavActions, onboardingNavResultRetrievers)
        citySelectorFeature(citySelectorFeature, citySelectorNavActions)
        signInFeature(navController, signInFeature, signInNavActions)
        signUpFeature(navController, signUpFeature, SignUpFeature.NavActions)
        productListFeature(navController, productListFeature, productListNavActions)
        productFeature(productFeature, productNavActions)
        productSubscriptionFeature(productSubscriptionFeature, productSubscriptionNavActions)
        detectedCityFeature(detectedCityFeature, detectedCityNavActions)
        webViewFeature(webViewFeature, webViewNavActions)
        searchFeature(navController, searchFeature, searchNavActions)
    }
}

private inline fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(
    lastSelectedBottomNavBarItem: BottomNavBarItem?,
    backStack: List<NavBackStackEntry>,
    defaultTransition: () -> EnterTransition,
    transitionBetweenBottomNavBarItems: () -> EnterTransition,
): EnterTransition {
    val targetDestinationBottomNavBarItem =
        targetDestination.findClosestBottomNavBarItem(backStack)
    return when {
        lastSelectedBottomNavBarItem == null || targetDestinationBottomNavBarItem == null -> {
            defaultTransition()
        }

        lastSelectedBottomNavBarItem == targetDestinationBottomNavBarItem -> {
            defaultTransition()
        }

        else -> transitionBetweenBottomNavBarItems()
    }
}

private inline fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(
    lastSelectedBottomNavBarItem: BottomNavBarItem?,
    backStack: List<NavBackStackEntry>,
    defaultTransition: () -> ExitTransition,
    transitionBetweenBottomNavBarItems: () -> ExitTransition,
): ExitTransition {
    val targetDestinationBottomNavBarItem =
        targetDestination.findClosestBottomNavBarItem(backStack)
    return when {
        lastSelectedBottomNavBarItem == null || targetDestinationBottomNavBarItem == null -> {
            defaultTransition()
        }

        lastSelectedBottomNavBarItem == targetDestinationBottomNavBarItem -> {
            defaultTransition()
        }

        else -> transitionBetweenBottomNavBarItems()
    }
}

private fun NavDestination.findClosestBottomNavBarItem(
    backStack: List<NavBackStackEntry>,
): BottomNavBarItem? {
    val destinationIndexInBackStack = backStack.indexOfLast { it.destination == this }
    if (destinationIndexInBackStack == -1) return null

    var resultItem: BottomNavBarItem? = null
    outer@for (i in destinationIndexInBackStack downTo 0) {
        val backStackEntry = backStack[i]
        val destination = backStackEntry.destination
        for (item in BottomNavBarItems) {
            if (destination.hasRoute(item.toFeatureNavEntry()::class)) {
                resultItem = item
                break@outer
            }
        }
    }
    return resultItem
}
