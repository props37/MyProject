package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.ui.navigation.destinations.Home
import ru.zarina.zarina.ui.screens.home.HomeScreen

fun NavGraphBuilder.homeGraph(
    navController: NavController,
) {
    navigationGraph(Home) {
        composableDestination(Home.Root) {
            HomeScreen(
                showProduct = { product ->
                    val arguments = Destinations.Product.Arguments(productId = product.id)
                    navController.navigate(Destinations.Product.createRoute(arguments))
                }
            )
        }
    }
}
