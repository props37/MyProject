package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.api.ProductNavActions
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavParams

fun NavGraphBuilder.productFeature(
    feature: ProductFeature,
    actions: ProductNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = Unit,
        )
    }
}

@Composable
fun rememberProductNavActions(
    navController: NavHostController
): ProductNavActions {
    return remember(navController) {
        ProductNavActions(
            onBackClicked = { navController.navigateUp() },
            onSubscribeToProductClicked = { product, offer ->
                val productSubscriptionParams = ProductSubscriptionNavParams(product, offer)
                val productSubscriptionNavEntry = productSubscriptionParams.toNavEntry()
                navController.navigate(productSubscriptionNavEntry)
            },
        )
    }
}
