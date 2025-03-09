package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature
import ru.livetyping.zarina.presentation.navigation.util.fadeInTransition
import ru.livetyping.zarina.presentation.navigation.util.fadeOutTransition
import ru.livetyping.zarina.presentation.navigation.util.initialDestination
import ru.livetyping.zarina.presentation.navigation.util.targetDestination

fun NavGraphBuilder.searchFeature(
    navController: NavHostController,
    feature: SearchFeature,
    actions: SearchFeature.NavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
            enterTransition = {
                when {
                    initialDestination.hasRoute<CatalogFeature.NavEntry.StartNavEntry>() -> fadeInTransition()
                    else -> null
                }
            },
            popExitTransition = {
                when {
                    targetDestination.hasRoute<CatalogFeature.NavEntry.StartNavEntry>() -> fadeOutTransition()
                    else -> null
                }
            },
        )
    }
}

@Composable
fun rememberSearchNavActions(
    navController: NavHostController
): SearchFeature.NavActions {
    return remember(navController) {
        SearchFeature.NavActions(
            onBackClicked = { navController.navigateUp() },
            onCategoryClicked = { categoryId ->
                val productListNavEntry = ProductListFeature.NavEntry.create(categoryId)
                navController.navigate(productListNavEntry)
            },
            onProductClicked = { product ->
                val productNavEntry = ProductFeature.NavEntry.create(product.id)
                navController.navigate(productNavEntry)
            },
            onSubscribeToProductClicked = { product, offer ->
                val productSubscriptionNavEntry =
                    ProductSubscriptionFeature.NavEntry.create(product, offer)
                navController.navigate(productSubscriptionNavEntry)
            },
        )
    }
}
