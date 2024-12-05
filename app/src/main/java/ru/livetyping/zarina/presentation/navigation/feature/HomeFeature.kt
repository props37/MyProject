package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigationutil.hasRoute
import ru.livetyping.zarina.core.navigationutil.withParent
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterFadeInTransition
import ru.livetyping.zarina.feature.home.domain.model.ClickAction
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.HomeNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavParams
import ru.livetyping.zarina.presentation.navigation.util.initialDestination

fun NavGraphBuilder.homeFeature(
    feature: HomeFeature,
    actions: HomeNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = Unit,
            enterTransition = {
                val initialDestinationWithParent = initialDestination.withParent()
                when {
                    initialDestinationWithParent.hasRoute(OnboardingFeature.getNavEntryClass()) -> {
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
): HomeNavActions {
    return remember(navController) {
        HomeNavActions(
            onBannerClicked = { banner ->
                when (val clickAction = banner.clickAction) {
                    is ClickAction.Products -> {
                        val productListParams = ProductListNavParams(
                            categoryId = clickAction.categoryId,
                        )
                        val productListNavEntry = productListParams.toNavEntry()
                        navController.navigate(productListNavEntry)
                    }

                    null -> Unit
                }
            },
        )
    }
}
