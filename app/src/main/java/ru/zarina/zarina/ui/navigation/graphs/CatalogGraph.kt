package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Catalog
import ru.zarina.zarina.ui.screens.catalog.categories.CategoriesScreen
import ru.zarina.zarina.ui.screens.catalog.products.ProductsScreen

fun NavGraphBuilder.catalogGraph(
    navController: NavController,
) {
    navigationGraph(Catalog) {
        composableDestination(Catalog.Categories) {
            CategoriesScreen(
                showProducts = { categoryId ->
                    val arguments = Catalog.Products.Arguments(categoryId)
                    navController.navigate(Catalog.Products.createRoute(arguments))
                },
            )
        }
        composableDestination(Catalog.Products) {
            ProductsScreen(
                goBack = {
                    navController.popBackStack(Catalog.Products.routeSchema, true)
                }
            )
        }
    }
}
