package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigationutil.hasRoute
import ru.livetyping.zarina.core.navigationutil.withParent
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterFadeInTransition
import ru.livetyping.zarina.feature.home.domain.model.ClickAction
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.webview.ui.WebViewFeature
import ru.livetyping.zarina.presentation.navigation.util.initialDestination

fun NavGraphBuilder.homeFeature(
    navController: NavHostController,
    feature: HomeFeature,
    actions: HomeFeature.NavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
            enterTransition = {
                val initialDestinationWithParent = initialDestination.withParent()
                when {
                    initialDestinationWithParent.hasRoute(OnboardingFeature.NavEntry::class) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
        )
    }
}

@Composable
fun rememberHomeNavActions(
    navController: NavHostController
): HomeFeature.NavActions {
    return remember(navController) {
        HomeFeature.NavActions(
            onBannerClicked = { banner ->
                when (val clickAction = banner.clickAction) {
                    is ClickAction.OpenProductList -> {
                        val productListNavEntry =
                            ProductListFeature.NavEntry.create(clickAction.categoryId)
                        navController.navigate(productListNavEntry)
                    }

                    is ClickAction.OpenUrl -> {
                        val webViewNavEntry = WebViewFeature.NavEntry(clickAction.url.value)
                        navController.navigate(webViewNavEntry)
                    }

                    null -> Unit
                }
            },
        )
    }
}
