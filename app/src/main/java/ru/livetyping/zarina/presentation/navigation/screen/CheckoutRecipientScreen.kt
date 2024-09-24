package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.recipient.CheckoutRecipientScreen
import ru.livetyping.zarina.presentation.screen.checkout.recipient.CheckoutRecipientScreenAction

fun NavGraphBuilder.checkoutRecipientScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.Recipient) {
        CheckoutRecipientScreen(
            navigate = { action ->
                when (action) {
                    CheckoutRecipientScreenAction.CheckoutClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CheckoutRecipientScreenAction.RecipientValidated -> {
                        when (action.cartType) {
                            CartType.DELIVERY -> {
                                navController.navigateToCheckoutDeliveryMethodScreen(
                                    cartType = action.cartType,
                                    step = action.step,
                                )
                            }

                            CartType.PICKUP -> {
                                val pickupStoreSelection = CheckoutGraph.PickupStoreSelection(
                                    cartType = CartTypeParcelable.from(action.cartType),
                                    step = action.step,
                                    deliveryMethodType = DeliveryMethodTypeParcelable.from(DeliveryMethodType.RETAIL),
                                )
                                navController.navigate(pickupStoreSelection)
                            }
                        }
                    }
                }
            },
        )
    }
}
