package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.payment.PaymentScreen
import ru.livetyping.zarina.presentation.screen.payment.PaymentScreenAction

fun NavGraphBuilder.paymentScreen(navController: NavHostController) {
    composable<UnscopedDestinations.Payment>(
        enterTransition = { slideEnterTransition() },
        exitTransition = { slideExitTransition() },
        popEnterTransition = { slidePopEnterTransition() },
        popExitTransition = { slidePopExitTransition() },
    ) {
        PaymentScreen(
            navigate = { action ->
                when (action) {
                    PaymentScreenAction.ScreenClosed -> {
                        navController.popBackStack<UnscopedDestinations.Payment>(inclusive = true)
                        val result = UnscopedDestinations.Payment.Result()
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.Payment.RESULT_KEY, result)
                    }
                }
            },
        )
    }
}
