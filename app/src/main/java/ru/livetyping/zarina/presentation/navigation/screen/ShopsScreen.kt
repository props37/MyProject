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
import ru.livetyping.zarina.presentation.screen.stores.StoresScreen
import ru.livetyping.zarina.presentation.screen.stores.StoresScreenAction

fun NavGraphBuilder.storesScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.Stores,
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
        StoresScreen(
            navigate = { action ->
                when (action) {
                    StoresScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.Stores.routeSchema,
                            inclusive = true,
                        )
                    }

                    StoresScreenAction.LocationPermissionRequired -> {
                        navController.navigateToPermissionRequirement(
                            permission = UnscopedDestinations.PermissionRequirement.Permission.LOCATION,
                            title = Text.Resource(R.string.grant_location_permission),
                            body = Text.Resource(R.string.it_will_help_us_to_detect_your_location),
                        )
                    }

                    is StoresScreenAction.StoreClicked -> {
                        val store = action.store
                        val title = Text.String(store.name)
                        val bodyString = buildString {
                            append(store.address)
                            if (!store.schedule.isNullOrBlank()) {
                                append(NEW_LINE)
                                append(store.schedule)
                            }
                            if (!store.phone?.value.isNullOrBlank()) {
                                append(NEW_LINE)
                                append(store.phone?.value)
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

fun NavHostController.navigateToStoresScreen() {
    this.navigate(ProfileGraph.Stores.route)
}

private const val NEW_LINE = "\n"
