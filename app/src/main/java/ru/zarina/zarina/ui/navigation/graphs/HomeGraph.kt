package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Catalog
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.ui.navigation.destinations.Home
import ru.zarina.zarina.ui.navigation.destinations.Search
import ru.zarina.zarina.ui.screens.home.HomeScreen

fun NavGraphBuilder.homeGraph(
    navController: NavController,
) {
    navigationGraph(Home) {
        composableDestination(Home.Root) {
            HomeScreen(
                showProduct = { id ->
                    val arguments = Destinations.Product.Arguments(productId = id)
                    navController.navigate(Destinations.Product.createRoute(arguments))
                },
                showProducts = { id, filtration ->
                    val arguments = Catalog.Products.Arguments(
                        categoryId = id,
                        filtration = filtration
                    )
                    navController.navigate(Catalog.Products.createRoute(arguments)) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                showWebpage = { url ->
                    val arguments = Destinations.Webpage.Arguments(url = url.value)
                    navController.navigate(Destinations.Webpage.createRoute(arguments))
                },
                showSearch = {
                    navController.navigate(Search.Root.routeSchema)
                }
            )
        }
    }
}
