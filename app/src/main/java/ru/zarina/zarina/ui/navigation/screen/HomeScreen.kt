package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.domain.common.ClickAction
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.graph.HomeGraph
import ru.zarina.zarina.ui.screen.home.HomeScreen
import ru.zarina.zarina.ui.screen.home.HomeScreenAction

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
