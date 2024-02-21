package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import ru.zarina.zarina.ui.model.product.ProductOfferParcelable
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorBottomSheetScreen
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorScreenAction
import timber.log.Timber
import java.util.UUID

fun NavGraphBuilder.sizeSelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.SizeSelector) {
        SizeSelectorBottomSheetScreen(
            navigate = { action ->
                when (action) {
                    SizeSelectorScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.SizeSelector.routeSchema,
                            inclusive = true,
                        )
                    }

                    is SizeSelectorScreenAction.SizeClicked -> {
                        val firstOffer = action.offers.firstOrNull()
                        when {
                            action.offers.size > 1 -> {
                                val args = UnscopedDestinations.HeightSelector.Args(
                                    product = action.product,
                                    offers = action.offers,
                                )
                                val route = UnscopedDestinations.HeightSelector.createRoute(args)
                                navController.navigate(route)
                            }

                            firstOffer != null && firstOffer.isAvailable -> {
                                navController.popBackStack(
                                    route = UnscopedDestinations.SizeSelector.routeSchema,
                                    inclusive = true,
                                )
                                val productParcelable = ProductParcelable.from(action.product)
                                val offerParcelable = ProductOfferParcelable.from(firstOffer)
                                val result = UnscopedDestinations.SizeSelector.Result(
                                    id = UUID.randomUUID().toString(),
                                    product = productParcelable,
                                    offer = offerParcelable,
                                )
                                navController.currentBackStackEntry?.savedStateHandle
                                    ?.set(UnscopedDestinations.SizeSelector.RESULT_KEY, result)
                            }

                            firstOffer != null && !firstOffer.isAvailable -> {
                                val args = UnscopedDestinations.ProductSubscription.Args(
                                    product = action.product,
                                    offer = firstOffer,
                                )
                                val route = UnscopedDestinations.ProductSubscription.createRoute(args)
                                val navOptions = navOptions {
                                    popUpTo(UnscopedDestinations.SizeSelector.routeSchema) {
                                        inclusive = true
                                    }
                                }
                                navController.navigate(route, navOptions)
                            }

                            firstOffer == null -> {
                                Timber.e("Could not perform navigation because offer is null")
                            }
                        }
                    }
                }
            },
        )
    }
}
