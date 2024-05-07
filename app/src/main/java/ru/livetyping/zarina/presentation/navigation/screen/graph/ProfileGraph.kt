package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.screen.accountDeletionConfirmationDialog
import ru.livetyping.zarina.presentation.navigation.screen.myOrdersScreen
import ru.livetyping.zarina.presentation.navigation.screen.orderCancellationDialog
import ru.livetyping.zarina.presentation.navigation.screen.orderScreen
import ru.livetyping.zarina.presentation.navigation.screen.profileDetailsScreen
import ru.livetyping.zarina.presentation.navigation.screen.profileScreen
import ru.livetyping.zarina.presentation.navigation.screen.shopsScreen
import ru.livetyping.zarina.presentation.navigation.screen.signOutConfirmationDialog

fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigationGraph(ProfileGraph) {
        profileScreen(navController)
        profileDetailsScreen(navController)
        signOutConfirmationDialog(navController)
        accountDeletionConfirmationDialog(navController)
        myOrdersScreen(navController)
        orderScreen(navController)
        orderCancellationDialog(navController)
        shopsScreen(navController)
    }
}
