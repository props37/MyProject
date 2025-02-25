package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature

fun NavGraphBuilder.productListFeature(
    navController: NavHostController,
    feature: ProductListFeature,
    actions: ProductListFeature.NavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
        )
    }
}

@Composable
fun rememberProductListNavActions(
    navController: NavHostController
): ProductListFeature.NavActions {
    return remember(navController) {
        ProductListFeature.NavActions(
            onBackClicked = { navController.navigateUp() },
            onSearchClicked = { navController.navigate(SearchFeature.NavEntry) },
            onProductClicked = { product ->
                val productNavEntry = ProductFeature.NavEntry.create(product.id)
                navController.navigate(productNavEntry)
            },
            onSubscribeToProductClicked = { product, offer ->
                val productSubscriptionNavEntry =
                    ProductSubscriptionFeature.NavEntry.create(product, offer)
                navController.navigate(productSubscriptionNavEntry)
            }
        )
    }
}
