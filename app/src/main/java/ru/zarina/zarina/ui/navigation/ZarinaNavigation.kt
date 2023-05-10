package ru.zarina.zarina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.ui.navigation.destinations.PickupDestination
import ru.zarina.zarina.ui.navigation.destinations.ProductDestination
import ru.zarina.zarina.ui.navigation.destinations.SelectSizeDestination
import ru.zarina.zarina.ui.screens.cityselection.CitySelectionScreen
import ru.zarina.zarina.ui.screens.home.HomeScreen
import ru.zarina.zarina.ui.screens.onboarding.OnboardingScreen
import ru.zarina.zarina.ui.screens.pickup.PickupScreen
import ru.zarina.zarina.ui.screens.product.ProductScreen
import ru.zarina.zarina.ui.screens.selectsize.SelectSizeScreen

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ZarinaNavigation(
    startDestination: Destination<*>,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            navController = navController,
            startDestination = startDestination.routeSchema,
        ) {
            composableDestination(Destinations.HOME) {
                HomeScreen(
                    showProduct = { productId ->
                        val arguments = ProductDestination.Arguments(
                            productId = productId,
                        )
                        navController.navigate(Destinations.PRODUCT.createRoute(arguments))
                    }
                )
            }
            composableDestination(Destinations.ONBOARDING) {
                OnboardingScreen(
                    showHome = {
                        navController.navigate(Destinations.HOME.route) { popUpTo(0) }
                    },
                    showCitySelection = {
                        navController.navigate(Destinations.CITY_SELECTION.route)
                    }
                )
            }
            composableDestination(Destinations.CITY_SELECTION) {
                CitySelectionScreen(
                    showHome = {
                        navController.navigate(Destinations.HOME.route) { popUpTo(0) }
                    }
                )
            }
            composableDestination(Destinations.PRODUCT) {
                ProductScreen(
                    showProduct = { productId ->
                        val arguments = ProductDestination.Arguments(
                            productId = productId,
                        )
                        navController.navigate(Destinations.PRODUCT.createRoute(arguments))
                    },
                    showPickup = { productId ->
                        val arguments = PickupDestination.Arguments(
                            productId = productId,
                        )
                        navController.navigate(Destinations.PICKUP.createRoute(arguments))
                    },
                    goBack = {
                        navController.popBackStack(Destinations.PRODUCT.routeSchema, true)
                    }
                )
            }
            composableDestination(Destinations.PICKUP) {
                PickupScreen(
                    showSelectSize = { productId ->
                        val arguments = SelectSizeDestination.Arguments(
                            productId = productId,
                        )
                        navController.navigate(Destinations.SELECT_SIZE.createRoute(arguments))
                    },
                    goBack = {
                        navController.popBackStack(Destinations.PICKUP.routeSchema, true)
                    }
                )
            }
            bottomSheetDestination(Destinations.SELECT_SIZE) {
                SelectSizeScreen()
            }
        }
    }
}
