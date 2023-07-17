package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Catalog
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.ui.navigation.destinations.Home
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
                    // TODO go to catalogue graph
                    val arguments = Catalog.Products.Arguments(
                        categoryId = id,
                        // TODO add filtration
                    )
                    navController.navigate(Catalog.Products.createRoute(arguments))
                },
                showWebpage = { url ->
                    val arguments = Destinations.Webpage.Arguments(url = url.value)
                    navController.navigate(Destinations.Webpage.createRoute(arguments))
                }
            )
        }
    }
}
