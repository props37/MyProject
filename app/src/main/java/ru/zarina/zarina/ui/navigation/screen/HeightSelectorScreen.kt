package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import ru.zarina.zarina.ui.model.product.ProductOfferParcelable
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorBottomSheetScreenScreen
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorScreenAction
import ru.zarina.zarina.util.library.navigation.navigate

fun NavGraphBuilder.heightSelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(SizeSelectorGraph.HeightSelector) {
        HeightSelectorBottomSheetScreenScreen(
            navigate = { action ->
                when (action) {
                    HeightSelectorScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = SizeSelectorGraph.HeightSelector.routeSchema,
                            inclusive = true,
                        )
                    }

                    HeightSelectorScreenAction.SizeSelectorFlowClosed -> {
                        navController.popBackStack(
                            route = SizeSelectorGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    is HeightSelectorScreenAction.OfferClicked -> {
                        if (action.offer.isAvailable) {
                            navController.popBackStack(
                                route = SizeSelectorGraph.routeSchema,
                                inclusive = true,
                            )
                            val productParcelable = ProductParcelable.from(action.product)
                            val offerParcelable = ProductOfferParcelable.from(action.offer)
                            val result = SizeSelectorGraph.Result(
                                product = productParcelable,
                                offer = offerParcelable,
                            )
                            navController.currentBackStackEntry?.savedStateHandle
                                ?.set(SizeSelectorGraph.RESULT_KEY, result)
                        } else {
                            val args = UnscopedDestinations.ProductSubscription.Args(
                                product = action.product,
                                offer = action.offer,
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
                    }
                }
            },
        )
    }
}
