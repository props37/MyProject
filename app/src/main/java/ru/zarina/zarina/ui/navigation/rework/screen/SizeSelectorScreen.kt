package ru.zarina.zarina.ui.navigation.rework.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import ru.zarina.zarina.ui.model.product.ProductOfferParcelable
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.rework.destination.graph.SizeSelectorGraph
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorBottomSheetScreen
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorScreenAction
import ru.zarina.zarina.util.library.navigation.navigate
import timber.log.Timber
import java.util.UUID

fun NavGraphBuilder.sizeSelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(SizeSelectorGraph.SizeSelector) {
        SizeSelectorBottomSheetScreen(
            navigate = { action ->
                when (action) {
                    SizeSelectorScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = SizeSelectorGraph.SizeSelector.routeSchema,
                            inclusive = true,
                        )
                    }

                    is SizeSelectorScreenAction.SizeClicked -> {
                        val firstOffer = action.offers.firstOrNull()
                        when {
                            action.offers.size > 1 -> {
                                val args = SizeSelectorGraph.HeightSelector.Args(
                                    product = action.product,
                                    offers = action.offers,
                                )
                                navController.navigate(
                                    route = SizeSelectorGraph.HeightSelector.routeSchema,
                                    args = SizeSelectorGraph.HeightSelector.createArgsBundle(args),
                                )
                            }

                            firstOffer != null && firstOffer.isAvailable -> {
                                navController.popBackStack(
                                    route = SizeSelectorGraph.routeSchema,
                                    inclusive = true,
                                )
                                val productParcelable = ProductParcelable.from(action.product)
                                val offerParcelable = ProductOfferParcelable.from(firstOffer)
                                val result = SizeSelectorGraph.Result(
                                    product = productParcelable,
                                    offer = offerParcelable,
                                )
                                navController.currentBackStackEntry?.savedStateHandle
                                    ?.set(SizeSelectorGraph.RESULT_KEY, result)
                            }

                            firstOffer != null && !firstOffer.isAvailable -> {
                                val args = UnscopedDestinations.ProductSubscription.Args(
                                    product = action.product,
                                    offer = firstOffer,
                                )
                                val navOptions = navOptions {
                                    popUpTo(SizeSelectorGraph.routeSchema) {
                                        inclusive = true
                                    }
                                }
                                navController.navigate(
                                    route = UnscopedDestinations.ProductSubscription.routeSchema,
                                    args = UnscopedDestinations.ProductSubscription.createArgsBundle(args),
                                    navOptions = navOptions,
                                )
                            }

                            firstOffer == null -> {
                                Timber.e("Could not perform navigation because product offer is null")
                            }
                        }
                    }
                }
            },
        )
    }
}
