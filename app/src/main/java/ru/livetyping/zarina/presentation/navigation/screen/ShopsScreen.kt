package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.shops.ShopsScreen
import ru.livetyping.zarina.presentation.screen.shops.ShopsScreenAction

fun NavGraphBuilder.shopsScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.Shops,
        enterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.Profile.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                ProfileGraph.Profile.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        ShopsScreen(
            navigate = { action ->
                when (action) {
                    ShopsScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.Shops.routeSchema,
                            inclusive = true,
                        )
                    }

                    ShopsScreenAction.LocationPermissionRequired -> {
                        navController.navigateToPermissionRequirement(
                            permission = UnscopedDestinations.PermissionRequirement.Permission.LOCATION,
                            title = Text.Resource(R.string.grant_location_permission),
                            body = Text.Resource(R.string.it_will_help_us_to_detect_your_location),
                        )
                    }

                    is ShopsScreenAction.ShopClicked -> {
                        val shop = action.shop
                        val title = Text.String(shop.name)
                        val bodyString = buildString {
                            append(shop.address)
                            if (!shop.schedule.isNullOrBlank()) {
                                append(NEW_LINE)
                                append(shop.schedule)
                            }
                            if (!shop.phone?.value.isNullOrBlank()) {
                                append(NEW_LINE)
                                append(shop.phone?.value)
                            }
                        }
                        val body = Text.String(bodyString)
                        navController.navigateToGenericBottomSheetScreen(title, body)
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToShopsScreen() {
    this.navigate(ProfileGraph.Shops.route)
}

private const val NEW_LINE = "\n"
