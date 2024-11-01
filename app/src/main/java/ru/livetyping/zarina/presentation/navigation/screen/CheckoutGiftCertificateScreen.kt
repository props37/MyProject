package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.giftcert.CheckoutGiftCertificateScreen
import ru.livetyping.zarina.presentation.screen.checkout.giftcert.CheckoutGiftCertificateScreenAction

fun NavGraphBuilder.checkoutGiftCertificateScreen(navController: NavHostController) {
    composable<CheckoutGraph.GiftCertificate> {
        CheckoutGiftCertificateScreen(
            navigate = { action ->
                when (action) {
                    CheckoutGiftCertificateScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.GiftCertificate>(inclusive = true)
                    }
                }
            },
        )
    }
}
