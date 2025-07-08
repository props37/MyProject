package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.popBackStackToBottomNavBarItem

fun NavGraphBuilder.wishlistFeature(
    navController: NavHostController,
    feature: WishlistFeature,
    actions: WishlistFeature.NavActions,
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
fun rememberWishlistNavActions(
    navController: NavHostController
): WishlistFeature.NavActions {
    return remember(navController) {
        WishlistFeature.NavActions(
            onBackClicked = { navController.navigateToBottomNavBarItem(BottomNavBarItem.Home) },
            onGoToCatalogClicked = {
                val bottomNavBarItem = BottomNavBarItem.Catalog
                navController.navigateToBottomNavBarItem(bottomNavBarItem)
                navController.popBackStackToBottomNavBarItem(bottomNavBarItem)
            },
            onProductClicked = { product ->
                val productNavEntry = ProductFeature.NavEntry.create(product.id)
                navController.navigate(productNavEntry)
            },
            onCategoryShortcutClicked = { categoryId ->
                val navItem = BottomNavBarItem.Catalog
                navController.navigateToBottomNavBarItem(navItem)
                navController.popBackStackToBottomNavBarItem(navItem)
                val productListNavEntry = ProductListFeature.NavEntry.create(categoryId)
                navController.navigate(productListNavEntry)
            },
            onSearchClicked = {
                val navItem = BottomNavBarItem.Catalog
                navController.navigateToBottomNavBarItem(navItem)
                navController.popBackStackToBottomNavBarItem(navItem)
                navController.navigate(SearchFeature.NavEntry)
            },
        )
    }
}
