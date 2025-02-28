package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem
import ru.livetyping.zarina.presentation.navigation.util.fadeInTransition
import ru.livetyping.zarina.presentation.navigation.util.fadeOutTransition
import ru.livetyping.zarina.presentation.navigation.util.initialDestination
import ru.livetyping.zarina.presentation.navigation.util.targetDestination

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
            exitTransition = {
                when {
                    targetDestination.hasRoute<SearchFeature.NavEntry>() -> fadeOutTransition()
                    else -> null
                }
            },
            popEnterTransition = {
                when {
                    initialDestination.hasRoute<SearchFeature.NavEntry>() -> fadeInTransition()
                    else -> null
                }
            },
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
