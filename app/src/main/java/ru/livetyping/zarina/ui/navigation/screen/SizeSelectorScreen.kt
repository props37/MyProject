package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import ru.livetyping.zarina.ui.model.product.ProductOfferParcelable
import ru.livetyping.zarina.ui.model.product.ProductParcelable
import ru.livetyping.zarina.ui.navigation.base.bottomSheetDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.ui.screen.sizeselector.SizeSelectorBottomSheetScreen
import ru.livetyping.zarina.ui.screen.sizeselector.SizeSelectorScreenAction
import timber.log.Timber

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
                                navController.navigateToHeightSelectorScreen(
                                    product = action.product,
                                    offers = action.offers,
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
                                val navOptions = navOptions {
                                    popUpTo(SizeSelectorGraph.routeSchema) {
                                        inclusive = true
                                    } 
                                }
                                navController.navigateToProductSubscriptionScreen(
                                    product = action.product,
                                    offer = firstOffer,
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
