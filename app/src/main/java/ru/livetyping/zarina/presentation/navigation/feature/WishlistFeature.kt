package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavActions
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem

fun NavGraphBuilder.wishlistFeature(
    feature: WishlistFeature,
    actions: WishlistNavActions,
) {
    with(feature) {
        composable(actions = actions)
    }
}

@Composable
fun rememberWishlistNavActions(
    navController: NavHostController
): WishlistNavActions {
    return remember(navController) {
        WishlistNavActions(
            onGoToCatalogClicked = {
                navController.navigateToBottomNavBarItem(BottomNavBarItem.Catalog)
                navController.popBackStack(
                    route = CatalogFeature.getNavEntry(),
                    inclusive = false,
                )
            },
        )
    }
}
