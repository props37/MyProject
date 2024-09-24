package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.model.checkout.CheckoutParamsParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointScreen
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointScreenAction

fun NavGraphBuilder.checkoutSelectedPickupPointScreen(navController: NavHostController) {
    composable<CheckoutGraph.SelectedPickupPoint>(
        typeMap = CheckoutGraph.SelectedPickupPoint.typeMap(),
    ) {
        SelectedPickupPointScreen(
            navigate = { action ->
                when (action) {
                    SelectedPickupPointScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.SelectedPickupPoint>(
                            inclusive = true,
                        )
                    }

                    is SelectedPickupPointScreenAction.ContinueClicked -> {
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
