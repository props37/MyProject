package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignUpGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.navigateToSignInGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.navigateToSignUpGraph
import ru.livetyping.zarina.presentation.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.screen.profile.ProfileScreen
import ru.livetyping.zarina.presentation.screen.profile.ProfileScreenAction
import ru.livetyping.zarina.presentation.screen.profile.ProfileViewModel

fun NavGraphBuilder.profileScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.Profile,
        exitTransition = {
            when (targetState.destination.route) {
                SignInGraph.SignIn.routeSchema,
                SignUpGraph.SignUp.routeSchema,
                ProfileGraph.MyOrders.routeSchema,
                ProfileGraph.ProfileDetails.routeSchema,
                ProfileGraph.Stores.routeSchema -> slideExitTransition()

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                SignInGraph.SignIn.routeSchema,
                SignInGraph.Otp.routeSchema,
                SignUpGraph.SignUp.routeSchema,
                SignUpGraph.Otp.routeSchema,
                ProfileGraph.MyOrders.routeSchema,
                ProfileGraph.ProfileDetails.routeSchema,
                ProfileGraph.Stores.routeSchema -> slidePopEnterTransition()

                else -> null
            }
        },
    ) {
        BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

        ProfileScreen(
            viewModel = hiltViewModel { factory: ProfileViewModel.Factory ->
                factory.create(it.savedStateHandle)
            },
            navigate = { action ->
                when (action) {
                    ProfileScreenAction.ProfileDetailsClicked -> {
                        navController.navigateToProfileDetailsScreen()
                    }

                    ProfileScreenAction.SignInClicked -> navController.navigateToSignInGraph()
                    ProfileScreenAction.SignUpClicked -> navController.navigateToSignUpGraph()
                    is ProfileScreenAction.CityClicked -> {
                        navController.navigateToCitySelectorScreen(
                            currentCity = action.currentCity,
                            title = Text.Resource(R.string.city_change),
                        )
                    }

                    ProfileScreenAction.MyOrdersClicked -> navController.navigateToMyOrdersScreen()
                    ProfileScreenAction.StoresClicked -> navController.navigateToStoresScreen()
                }
            }
        )
    }
}
