package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavActions
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavParams

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
        )
    }
}
