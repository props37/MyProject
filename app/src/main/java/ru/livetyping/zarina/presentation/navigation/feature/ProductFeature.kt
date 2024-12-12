package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.api.ProductNavActions

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
        ProductNavActions()
    }
}
