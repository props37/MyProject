package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem

fun NavGraphBuilder.catalogFeature(
    navController: NavHostController,
    feature: CatalogFeature,
    actions: CatalogFeature.NavActions,
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
fun rememberCatalogNavActions(
    navController: NavHostController
): CatalogFeature.NavActions {
    return remember(navController) {
        CatalogFeature.NavActions(
            onBackClicked = { navController.navigateToBottomNavBarItem(BottomNavBarItem.Home) },
            onSearchClicked = { navController.navigate(SearchFeature.NavEntry) },
            onCategoryClicked = { categoryId ->
                val productListNavEntry = ProductListFeature.NavEntry.create(categoryId)
                navController.navigate(productListNavEntry)
            }
        )
    }
}
