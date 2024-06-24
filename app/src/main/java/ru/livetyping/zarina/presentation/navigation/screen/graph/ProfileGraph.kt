package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.screen.accountDeletionConfirmationDialog
import ru.livetyping.zarina.presentation.navigation.screen.changePasswordScreen
import ru.livetyping.zarina.presentation.navigation.screen.myOrdersScreen
import ru.livetyping.zarina.presentation.navigation.screen.orderCancellationDialog
import ru.livetyping.zarina.presentation.navigation.screen.orderScreen
import ru.livetyping.zarina.presentation.navigation.screen.profileDetailsScreen
import ru.livetyping.zarina.presentation.navigation.screen.profileScreen
import ru.livetyping.zarina.presentation.navigation.screen.signOutConfirmationDialog
import ru.livetyping.zarina.presentation.navigation.screen.storesScreen

fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigationGraph(ProfileGraph) {
        profileScreen(navController)
        profileDetailsScreen(navController)
        changePasswordScreen(navController)
        signOutConfirmationDialog(navController)
        accountDeletionConfirmationDialog(navController)
        myOrdersScreen(navController)
        orderScreen(navController)
        orderCancellationDialog(navController)
        storesScreen(navController)
    }
}
