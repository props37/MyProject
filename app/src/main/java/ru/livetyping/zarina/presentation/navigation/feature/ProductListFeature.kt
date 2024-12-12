package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.api.ProductNavParams
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavActions
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavParams
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavParams

fun NavGraphBuilder.productListFeature(
    feature: ProductListFeature,
    actions: ProductListNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = Unit,
        )
    }
}

@Composable
fun rememberProductListNavActions(
    navController: NavHostController
): ProductListNavActions {
    return remember(navController) {
        ProductListNavActions(
            onBackClicked = { navController.navigateUp() },
            onTagClicked = { category, filters ->
                val productListParams = ProductListNavParams(
                    categoryId = category.id,
                    filters = filters,
                )
                val productListNavEntry = ProductListFeature.getNavEntry(productListParams)
                navController.navigate(productListNavEntry)
            },
            onProductClicked = { product ->
                val productParams = ProductNavParams(product.id)
                val productNavEntry = ProductFeature.getNavEntry(productParams)
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
