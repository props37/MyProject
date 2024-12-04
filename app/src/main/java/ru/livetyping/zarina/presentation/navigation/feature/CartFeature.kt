package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.cart.ui.api.CartNavActions
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem

fun NavGraphBuilder.cartFeature(
    navController: NavHostController,
    feature: CartFeature,
    actions: CartNavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
        )
    }
}

@Composable
fun rememberCartNavActions(
    navController: NavHostController
): CartNavActions {
    return remember(navController) {
        CartNavActions(
            onBackClicked = { navController.navigateToBottomNavBarItem(BottomNavBarItem.Home) },
        )
    }
}
