package ru.zarina.zarina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import ru.zarina.zarina.ui.screens.home.HomeScreen
import ru.zarina.zarina.ui.screens.onboarding.OnboardingScreen
import ru.zarina.zarina.ui.screens.pickup.root.PickupRootScreen
import ru.zarina.zarina.ui.screens.pickup.selectshopcity.SelectPickupCityScreen
import ru.zarina.zarina.ui.screens.pickup.selectsize.SelectSizeScreen
import ru.zarina.zarina.ui.screens.product.ProductScreen
import ru.zarina.zarina.ui.screens.selectcity.SelectCityScreen

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
                    showSelectCity = {
                        navController.navigate(Destinations.SelectCity.route)
                    }
                )
            }
            composableDestination(Destinations.SelectCity) {
                SelectCityScreen(
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
                    val parentEntry =
                        remember(it) { navController.getBackStackEntry(Pickup.routeSchema) }
                    PickupRootScreen(
                        parentEntry = parentEntry,
                        showSelectSize = {
                            navController.navigate(Pickup.SelectSize.route)
                        },
                        showSelectCity = {
                            navController.navigate(Pickup.SelectCity.route)
                        },
                        goBack = {
                            navController.popBackStack(Pickup.routeSchema, true)
                        }
                    )
                }
                bottomSheetDestination(Pickup.SelectSize) {
                    val parentEntry =
                        remember(it) { navController.getBackStackEntry(Pickup.routeSchema) }
                    SelectSizeScreen(
                        parentEntry = parentEntry,
                        goBack = {
                            navController.popBackStack(Pickup.SelectSize.routeSchema, true)
                        }
                    )
                }
                composableDestination(Pickup.SelectCity) {
                    val parentEntry =
                        remember(it) { navController.getBackStackEntry(Pickup.routeSchema) }
                    SelectPickupCityScreen(
                        parentEntry = parentEntry,
                    )
                }
            }
        }
    }
}
