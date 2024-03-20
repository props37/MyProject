package ru.zarina.zarina.ui.navigation.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.NavigationTransitionDurationMillis
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreen
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreenAction

fun NavGraphBuilder.productSubscriptionScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ProductSubscription,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        },
    ) {
        ProductSubscriptionScreen(
            navigate = { action ->
                when (action) {
                    ProductSubscriptionScreenAction.ScreenClosed,
                    ProductSubscriptionScreenAction.SubscriptionCompleted -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.ProductSubscription.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}
