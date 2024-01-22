package ru.zarina.zarina.ui.navigation.rework.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.domain.rework.common.ClickAction
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.rework.graph.HomeGraph
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.home.HomeScreen
import ru.zarina.zarina.ui.screen.home.HomeScreenAction

fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigationGraph(HomeGraph) {
        composableDestination(HomeGraph.Home) {
            HomeScreen(
                navigateForward = { action ->
                    when (action) {
                        is HomeScreenAction.BannerClicked -> {
                            when (action.banner.clickAction) {
                                is ClickAction.Products -> {
                                    val categoryId = action.banner.clickAction.categoryId
                                    val args = UnscopedDestinations.Products.Args(categoryId.value)
                                    val route = UnscopedDestinations.Products.createRoute(args)
                                    navController.navigate(route)
                                }

                                null -> Unit
                            }
                        }
                    }
                },
            )
        }
    }
}
