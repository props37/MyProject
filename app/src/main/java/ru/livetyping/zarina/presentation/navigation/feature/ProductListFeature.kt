package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavActions

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
        ProductListNavActions()
    }
}
