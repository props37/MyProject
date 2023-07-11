package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Cart
import ru.zarina.zarina.ui.screens.cart.CartScreen

fun NavGraphBuilder.cartGraph(navController: NavController) {
    navigationGraph(Cart) {
        composableDestination(Cart.Root) {
            CartScreen()
        }
    }
}
