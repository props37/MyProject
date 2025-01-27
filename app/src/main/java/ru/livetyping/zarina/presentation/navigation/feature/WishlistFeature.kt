package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavParams
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
            onSubscribeToProductClicked = { product, offer ->
                val productSubscriptionParams = ProductSubscriptionNavParams(product, offer)
                val productSubscriptionNavEntry =
                    ProductSubscriptionFeature.getNavEntry(productSubscriptionParams)
                navController.navigate(productSubscriptionNavEntry)
            },
        )
    }
}
