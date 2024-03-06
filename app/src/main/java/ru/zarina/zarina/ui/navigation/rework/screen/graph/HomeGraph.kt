package ru.zarina.zarina.ui.navigation.rework.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.domain.rework.common.ClickAction
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.rework.destination.graph.HomeGraph
import ru.zarina.zarina.ui.screen.home.HomeScreen
import ru.zarina.zarina.ui.screen.home.HomeScreenAction
import ru.zarina.zarina.util.library.navigation.navigate

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
                                    val args = UnscopedDestinations.Products.Args(categoryId)
                                    navController.navigate(
                                        route = UnscopedDestinations.Products.routeSchema,
                                        args = UnscopedDestinations.Products.createArgsBundle(args),
                                    )
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
