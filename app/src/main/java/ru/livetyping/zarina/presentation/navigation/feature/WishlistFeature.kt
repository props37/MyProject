package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavActions
import ru.livetyping.zarina.presentation.feature.Features

fun NavGraphBuilder.wishlistFeature(
    feature: WishlistFeature,
    actions: WishlistNavActions,
) {
    with(feature) {
        composable(actions)
    }
}

@Composable
fun rememberWishlistNavActions(
    features: Features,
    navController: NavHostController
): WishlistNavActions {
    return remember(features, navController) {
        WishlistNavActions()
    }
}
