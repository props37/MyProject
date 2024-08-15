package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.navigation.screen.checkoutCourierDeliveryScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutDeliveryMethodScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutRecipientScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutSelectedStoreScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutStoreSelectionScreen
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutGraph(navController: NavHostController) {
    navigationGraph(
        graph = CheckoutGraph,
        enterTransition = { slideEnterTransition() },
        exitTransition = { slideExitTransition() },
        popEnterTransition = { slidePopEnterTransition() },
        popExitTransition = { slidePopExitTransition() },
    ) {
        checkoutRecipientScreen(navController)
        checkoutStoreSelectionScreen(navController)
        checkoutSelectedStoreScreen(navController)
        checkoutDeliveryMethodScreen(navController)
        checkoutCourierDeliveryScreen(navController)
    }
}

fun NavHostController.navigateToCheckoutGraph(cartType: CartType) {
    val args = CheckoutGraph.Recipient.Args(cartType)
    this.navigate(
        route = CheckoutGraph.routeSchema,
        args = CheckoutGraph.createArgsBundle(args),
    )
}
