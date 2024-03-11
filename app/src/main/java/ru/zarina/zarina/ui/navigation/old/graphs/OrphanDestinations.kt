package ru.zarina.zarina.ui.navigation.old.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.old.destinations.Destinations
import ru.zarina.zarina.ui.navigation.old.destinations.Home
import ru.zarina.zarina.ui.navigation.old.destinations.Pickup
import ru.zarina.zarina.ui.screens.onboarding.OnboardingScreen
import ru.zarina.zarina.ui.screens.product.ProductScreen
import ru.zarina.zarina.ui.screens.selectcity.SelectCityScreen
import ru.zarina.zarina.ui.screens.webpage.WebpageScreen

fun NavGraphBuilder.orphans(
    navController: NavController,
    changeStartDestination: (Destination<*>) -> Unit,
) {
    composableDestination(Destinations.Onboarding) {
        OnboardingScreen(
            showHome = {
                changeStartDestination(Home)
            },
            showSelectCity = {
                navController.navigate(Destinations.SelectCity.route)
            }
        )
    }
    composableDestination(Destinations.SelectCity) {
        SelectCityScreen(
            showHome = {
                changeStartDestination(Home)
            }
        )
    }
    composableDestination(Destinations.Product) {
        ProductScreen(
            showProduct = { productId ->
                val arguments = Destinations.Product.Arguments(
                    productId = productId,
                )
                navController.navigate(Destinations.Product.createRoute(arguments))
            },
            showPickup = { productId ->
                val arguments = Pickup.Arguments(
                    productId = productId,
                )
                navController.navigate(Pickup.createRoute(arguments))
            },
            goBack = {
                navController.popBackStack(Destinations.Product.routeSchema, true)
            }
        )
    }
    composableDestination(Destinations.Webpage) {
        WebpageScreen(
            goBack = {
                navController.popBackStack(Destinations.Webpage.routeSchema, true)
            }
        )
    }
}
