package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.common.ClickAction
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.HomeGraph
import ru.livetyping.zarina.presentation.screen.home.HomeScreen
import ru.livetyping.zarina.presentation.screen.home.HomeScreenAction

fun NavGraphBuilder.homeScreen(navController: NavHostController) {
    composableDestination(HomeGraph.Home) {
        HomeScreen(
            navigateForward = { action ->
                when (action) {
                    is HomeScreenAction.BannerClicked -> {
                        when (action.banner.clickAction) {
                            is ClickAction.OpenProductList -> {
                                val categoryId = action.banner.clickAction.categoryId
                                navController.navigateToProductsScreen(categoryId)
                            }

                            is ClickAction.OpenUrl -> TODO() // TODO: [Top] Implement
                            null -> Unit
                        }
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToHomeScreen() {
    this.navigateToBottomNavBarItem(BottomNavBarItem.Home)
    this.popBackStack(
        route = HomeGraph.Home.routeSchema,
        inclusive = false,
    )
}
