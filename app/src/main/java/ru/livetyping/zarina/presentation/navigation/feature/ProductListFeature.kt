package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavParams

fun NavGraphBuilder.productListFeature(
    feature: ProductListFeature,
    actions: ProductListFeature.NavActions,
) {
    with(feature) {
        composable(
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
            onTagClicked = { category, filters ->
                val productListNavEntry = ProductListFeature.NavEntry.create(
                    categoryId = category.id,
                    filters = filters,
                )
                navController.navigate(productListNavEntry)
            },
            onProductClicked = { product ->
                val productNavEntry = ProductFeature.NavEntry.create(product.id)
                navController.navigate(productNavEntry)
            },
            onSubscribeToProductClicked = { product, offer ->
                val productSubscriptionParams = ProductSubscriptionNavParams(product, offer)
                val productSubscriptionNavEntry =
                    ProductSubscriptionFeature.getNavEntry(productSubscriptionParams)
                navController.navigate(productSubscriptionNavEntry)
            }
        )
    }
}
