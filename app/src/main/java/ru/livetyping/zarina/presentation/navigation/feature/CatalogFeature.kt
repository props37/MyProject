package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavActions
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavParams
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem

fun NavGraphBuilder.catalogFeature(
    navController: NavHostController,
    feature: CatalogFeature,
    actions: CatalogNavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
            resultRetrievers = Unit,
        )
    }
}

@Composable
fun rememberCatalogNavActions(
    navController: NavHostController
): CatalogNavActions {
    return remember(navController) {
        CatalogNavActions(
            onBackClicked = { navController.navigateToBottomNavBarItem(BottomNavBarItem.Home) },
            onCategoryClicked = { categoryId ->
                val productListParams = ProductListNavParams(categoryId)
                val productListNavEntry = productListParams.toNavEntry()
                navController.navigate(productListNavEntry)
            }
        )
    }
}
