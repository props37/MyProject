package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.model.checkout.CheckoutParamsParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore.CheckoutSelectedPickupStoreScreen
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore.CheckoutSelectedPickupStoreScreenAction

fun NavGraphBuilder.checkoutSelectedPickupStoreScreen(navController: NavHostController) {
    composable<CheckoutGraph.SelectedPickupStore>(
        typeMap = CheckoutGraph.SelectedPickupStore.typeMap(),
    ) {
        CheckoutSelectedPickupStoreScreen(
            navigate = { action ->
                when (action) {
                    CheckoutSelectedPickupStoreScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.SelectedPickupStore>(
                            inclusive = true,
                        )
                    }

                    is CheckoutSelectedPickupStoreScreenAction.ContinueClicked -> {
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
