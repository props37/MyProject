package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature

fun NavGraphBuilder.productFeature(
    feature: ProductFeature,
    actions: ProductFeature.NavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
        )
    }
}

@Composable
fun rememberProductNavActions(
    navController: NavHostController
): ProductFeature.NavActions {
    return remember(navController) {
        ProductFeature.NavActions(
            onBackClicked = { navController.navigateUp() },
            onSubscribeToProductClicked = { product, offer ->
                val productSubscriptionNavEntry =
                    ProductSubscriptionFeature.NavEntry.create(product, offer)
                navController.navigate(productSubscriptionNavEntry)
            },
        )
    }
}
