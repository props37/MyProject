package ru.zarina.zarina.ui.navigation.old.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.old.destinations.Catalog
import ru.zarina.zarina.ui.navigation.old.destinations.Destinations
import ru.zarina.zarina.ui.navigation.old.destinations.Home
import ru.zarina.zarina.ui.navigation.old.destinations.Search
import ru.zarina.zarina.ui.screens.home.HomeScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                    val encodedUrl =
                        URLEncoder.encode(url.value, StandardCharsets.UTF_8.toString())
                    val arguments = Destinations.Webpage.Arguments(url = encodedUrl)
                    navController.navigate(Destinations.Webpage.createRoute(arguments))
                },
                showSearch = {
                    navController.navigate(Search.Root.routeSchema)
                }
            )
        }
    }
}
