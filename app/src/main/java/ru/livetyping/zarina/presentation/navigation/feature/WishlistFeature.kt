package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigationutil.hasAnyRoute
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterFadeInTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaExitFadeOutTransition
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavActions
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItemNavEntryClasses
import ru.livetyping.zarina.presentation.navigation.util.initialDestination
import ru.livetyping.zarina.presentation.navigation.util.targetDestination

fun NavGraphBuilder.wishlistFeature(
    feature: WishlistFeature,
    actions: WishlistNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            enterTransition = {
                when {
                    initialDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
            exitTransition = {
                when {
                    targetDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaExitFadeOutTransition()
                    }

                    else -> null
                }
            },
            popEnterTransition = {
                when {
                    initialDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
            popExitTransition = {
                when {
                    targetDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaExitFadeOutTransition()
                    }

                    else -> null
                }
            },
        )
    }
}

@Composable
fun rememberWishlistNavActions(
    navController: NavHostController
): WishlistNavActions {
    return remember(navController) {
        WishlistNavActions()
    }
}
