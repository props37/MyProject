package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.payment.ui.api.PaymentFeature
import ru.livetyping.zarina.feature.payment.ui.api.PaymentResult

fun NavGraphBuilder.paymentFeature(
    navController: NavHostController,
    feature: PaymentFeature,
    actions: PaymentFeature.NavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
        )
    }
}

@Composable
fun rememberPaymentNavActions(
    navController: NavHostController
): PaymentFeature.NavActions {
    return remember(navController) {
        PaymentFeature.NavActions(
            onBackClicked = {
                navController.navigateUp()
                navController.currentBackStackEntry?.savedStateHandle
                    ?.set(PaymentResult.KEY, PaymentResult())
            },
        )
    }
}
