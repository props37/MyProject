package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigationutil.hasAnyRoute
import ru.livetyping.zarina.core.navigationutil.withParent
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterFadeInTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaExitFadeOutTransition
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.cart.ui.api.CartNavActions
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItemNavEntryClasses
import ru.livetyping.zarina.presentation.navigation.util.initialDestination
import ru.livetyping.zarina.presentation.navigation.util.targetDestination

fun NavGraphBuilder.cartFeature(
    navController: NavHostController,
    feature: CartFeature,
    actions: CartNavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
            enterTransition = {
                val initialDestinationWithParent = initialDestination.withParent()

                when {
                    initialDestinationWithParent.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
            exitTransition = {
                val targetDestinationWithParent = targetDestination.withParent()

                when {
                    targetDestinationWithParent.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaExitFadeOutTransition()
                    }

                    else -> null
                }
            },
            popEnterTransition = {
                val initialDestinationWithParent = initialDestination.withParent()

                when {
                    initialDestinationWithParent.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
            popExitTransition = {
                val targetDestinationWithParent = targetDestination.withParent()

                when {
                    targetDestinationWithParent.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaExitFadeOutTransition()
                    }

                    else -> null
                }
            },
        )
    }
}

@Composable
fun rememberCartNavActions(
    navController: NavHostController
): CartNavActions {
    return remember(navController) {
        CartNavActions()
    }
}
