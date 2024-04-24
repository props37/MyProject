package ru.livetyping.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.navigationGraph
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.navigation.screen.accountDeletionConfirmationDialog
import ru.livetyping.zarina.ui.navigation.screen.myOrdersScreen
import ru.livetyping.zarina.ui.navigation.screen.orderCancellationDialog
import ru.livetyping.zarina.ui.navigation.screen.orderScreen
import ru.livetyping.zarina.ui.navigation.screen.profileDetailsScreen
import ru.livetyping.zarina.ui.navigation.screen.profileScreen
import ru.livetyping.zarina.ui.navigation.screen.signOutConfirmationDialog

fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigationGraph(ProfileGraph) {
        profileScreen(navController)
        profileDetailsScreen(navController)
        signOutConfirmationDialog(navController)
        accountDeletionConfirmationDialog(navController)
        myOrdersScreen(navController)
        orderScreen(navController)
        orderCancellationDialog(navController)
    }
}
