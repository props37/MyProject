package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.ui.navigation.destinations.Home
import ru.zarina.zarina.ui.navigation.destinations.Pickup
import ru.zarina.zarina.ui.screens.onboarding.OnboardingScreen
import ru.zarina.zarina.ui.screens.product.ProductScreen
import ru.zarina.zarina.ui.screens.selectcity.SelectCityScreen
import ru.zarina.zarina.ui.screens.webpage.WebpageScreen

fun NavGraphBuilder.orphans(
    navController: NavController,
) {
    composableDestination(Destinations.Onboarding) {
        OnboardingScreen(
            showHome = {
                navController.navigate(Home.routeSchema) { popUpTo(0) }
            },
            showSelectCity = {
                navController.navigate(Destinations.SelectCity.route)
            }
        )
    }
    composableDestination(Destinations.SelectCity) {
        SelectCityScreen(
            showHome = {
                navController.popBackStack(0, true)
                navController.navigate(Home.routeSchema)
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
