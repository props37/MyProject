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
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.ui.navigation.destinations.Pickup
import ru.zarina.zarina.ui.screens.cityselection.CitySelectionScreen
import ru.zarina.zarina.ui.screens.home.HomeScreen
import ru.zarina.zarina.ui.screens.onboarding.OnboardingScreen
import ru.zarina.zarina.ui.screens.pickup.pickup.PickupScreen
import ru.zarina.zarina.ui.screens.pickup.selectsize.SelectSizeScreen
import ru.zarina.zarina.ui.screens.product.ProductScreen

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
            composableDestination(Destinations.Home) {
                HomeScreen(
                    showProduct = { productId ->
                        val arguments = Destinations.Product.Arguments(
                            productId = productId,
                        )
                        navController.navigate(Destinations.Product.createRoute(arguments))
                    }
                )
            }
            composableDestination(Destinations.Onboarding) {
                OnboardingScreen(
                    showHome = {
                        navController.navigate(Destinations.Home.route) { popUpTo(0) }
                    },
                    showCitySelection = {
                        navController.navigate(Destinations.CitySelection.route)
                    }
                )
            }
            composableDestination(Destinations.CitySelection) {
                CitySelectionScreen(
                    showHome = {
                        navController.navigate(Destinations.Home.route) { popUpTo(0) }
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
            navigationGraph(Pickup) {
                composableDestination(Pickup.Root) {
                    PickupScreen(
                        showSelectSize = { productId ->
                            val arguments = Pickup.SelectSize.Arguments(
                                productId = productId,
                            )
                            navController.navigate(
                                Pickup.SelectSize.createRoute(
                                    arguments
                                )
                            )
                        },
                        goBack = {
                            navController.popBackStack(Pickup.routeSchema, true)
                        }
                    )
                }
                bottomSheetDestination(Pickup.SelectSize) {
                    SelectSizeScreen()
                }
            }
        }
    }
}
