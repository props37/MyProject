package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.navigation.screen.checkoutCourierDeliveryDateTimeSelectorScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutCourierDeliveryScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutCustomerScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutDeliveryMethodScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutOrderPlacingScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutPickupPointDeliveryScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutPickupStoreSelectionScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutPostDeliveryScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutSelectedPickupPointScreen
import ru.livetyping.zarina.presentation.navigation.screen.checkoutSelectedPickupStoreScreen
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
        checkoutCustomerScreen(navController)
        checkoutPickupStoreSelectionScreen(navController)
        checkoutSelectedPickupStoreScreen(navController)
        checkoutDeliveryMethodScreen(navController)
        checkoutCourierDeliveryScreen(navController)
        checkoutCourierDeliveryDateTimeSelectorScreen(navController)
        checkoutPostDeliveryScreen(navController)
        checkoutPickupPointDeliveryScreen(navController)
        checkoutSelectedPickupPointScreen(navController)
        checkoutOrderPlacingScreen(navController)
    }
}

fun NavHostController.navigateToCheckoutGraph(cartType: CartType) {
    val args = CheckoutGraph.Customer.Args(cartType)
    this.navigate(
        route = CheckoutGraph.routeSchema,
        args = CheckoutGraph.createArgsBundle(args),
    )
}
