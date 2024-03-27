package ru.zarina.zarina.ui.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.zarina.zarina.ui.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.zarina.zarina.ui.screen.profile.ProfileScreen
import ru.zarina.zarina.ui.screen.profile.ProfileScreenAction
import ru.zarina.zarina.ui.screen.profile.ProfileViewModel

fun NavGraphBuilder.profileScreen(navController: NavHostController) {
    composableDestination(ProfileGraph.Profile) {
        BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

        ProfileScreen(
            viewModel = hiltViewModel { factory: ProfileViewModel.Factory ->
                factory.create(it.savedStateHandle)
            },
            navigate = { action ->
                when (action) {
                    is ProfileScreenAction.CityClicked -> {
                        navController.navigateToCitySelectorScreen(
                            currentCity = action.currentCity,
                            title = Text.Resource(R.string.city_change),
                        )
                    }
                }
            }
        )
    }
}
