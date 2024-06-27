package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.LoyaltyProgramGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignUpGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.navigateToLoyaltyProgramGraph
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
                LoyaltyProgramGraph.LoyaltyProgram.routeSchema,
                ProfileGraph.MyOrders.routeSchema,
                ProfileGraph.ProfileDetails.routeSchema,
                ProfileGraph.Stores.routeSchema,
                UnscopedDestinations.CitySelector.routeSchema -> slideExitTransition()

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                SignInGraph.SignIn.routeSchema,
                SignInGraph.Otp.routeSchema,
                SignUpGraph.SignUp.routeSchema,
                SignUpGraph.Otp.routeSchema,
                LoyaltyProgramGraph.LoyaltyProgram.routeSchema,
                ProfileGraph.MyOrders.routeSchema,
                ProfileGraph.ProfileDetails.routeSchema,
                ProfileGraph.Stores.routeSchema,
                UnscopedDestinations.CitySelector.routeSchema -> slidePopEnterTransition()

                else -> null
            }
        },
    ) {
        BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

        ProfileScreen(
            viewModel = hiltViewModel { factory: ProfileViewModel.Factory ->
                val citySelectorResultFlow = it.savedStateHandle
                    .getStateFlow<UnscopedDestinations.CitySelector.Result?>(
                        key = UnscopedDestinations.CitySelector.RESULT_KEY,
                        initialValue = null,
                    )
                factory.create(citySelectorResultFlow)
            },
            navigate = { action ->
                when (action) {
                    ProfileScreenAction.ProfileDetailsClicked -> {
                        navController.navigateToProfileDetailsScreen()
                    }

                    ProfileScreenAction.SignInClicked -> navController.navigateToSignInGraph()
                    ProfileScreenAction.SignUpClicked -> navController.navigateToSignUpGraph()

                    ProfileScreenAction.LoyaltyCardInfoClicked -> {
                        navController.navigateToLoyaltyProgramGraph()
                    }

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
