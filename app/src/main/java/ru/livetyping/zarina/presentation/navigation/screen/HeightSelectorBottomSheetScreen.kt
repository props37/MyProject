package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.presentation.model.product.ProductItemParcelable
import ru.livetyping.zarina.presentation.model.product.ProductOfferParcelable
import ru.livetyping.zarina.presentation.navigation.base.bottomSheetDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.presentation.screen.sizeselector.heightselector.HeightSelectorBottomSheetScreenScreen
import ru.livetyping.zarina.presentation.screen.sizeselector.heightselector.HeightSelectorScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

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
                            val productParcelable = ProductItemParcelable.from(action.product)
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

fun NavHostController.navigateToHeightSelectorScreen(
    product: Product,
    offers: List<ProductOffer>,
) {
    val args = SizeSelectorGraph.HeightSelector.Args(product, offers)
    this.navigate(
        route = SizeSelectorGraph.HeightSelector.routeSchema,
        args = SizeSelectorGraph.HeightSelector.createArgsBundle(args),
    )
}
