package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.model.checkout.CheckoutParamsParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.postdelivery.CheckoutPostDeliveryScreen
import ru.livetyping.zarina.presentation.screen.checkout.postdelivery.CheckoutPostDeliveryScreenAction

fun NavGraphBuilder.checkoutPostDeliveryScreen(navController: NavHostController) {
    composable<CheckoutGraph.PostDelivery>(
        typeMap = CheckoutGraph.PostDelivery.typeMap(),
    ) {
        CheckoutPostDeliveryScreen(
            navigate = { action ->
                when (action) {
                    CheckoutPostDeliveryScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.PostDelivery>(
                            inclusive = true,
                        )
                    }

                    CheckoutPostDeliveryScreenAction.CheckoutClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CheckoutPostDeliveryScreenAction.ContinueClicked -> {
                        val orderPlacing = CheckoutGraph.OrderPlacing(
                            step = action.step,
                            checkoutParams = CheckoutParamsParcelable.from(action.checkoutParams),
                        )
                        navController.navigate(orderPlacing)
                    }
                }
            },
        )
    }
}
