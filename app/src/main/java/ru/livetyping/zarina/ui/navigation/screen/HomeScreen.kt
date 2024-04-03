package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.common.ClickAction
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.HomeGraph
import ru.livetyping.zarina.ui.screen.home.HomeScreen
import ru.livetyping.zarina.ui.screen.home.HomeScreenAction

fun NavGraphBuilder.homeScreen(navController: NavHostController) {
    composableDestination(HomeGraph.Home) {
        HomeScreen(
            navigateForward = { action ->
                when (action) {
                    is HomeScreenAction.BannerClicked -> {
                        when (action.banner.clickAction) {
                            is ClickAction.Products -> {
                                val categoryId = action.banner.clickAction.categoryId
                                navController.navigateToProductsScreen(categoryId)
                            }

                            null -> Unit
                        }
                    }
                }
            },
        )
    }
}
