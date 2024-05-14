package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.navigation.base.bottomSheetDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.permissionrequirement.PermissionRequirementBottomSheetScreen
import ru.livetyping.zarina.presentation.screen.permissionrequirement.PermissionRequirementScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.permissionRequirementBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(
        destination = UnscopedDestinations.PermissionRequirement,
    ) {
        PermissionRequirementBottomSheetScreen(
            navigate = { action ->
                when (action) {
                    PermissionRequirementScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.PermissionRequirement.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToPermissionRequirement(
    permission: UnscopedDestinations.PermissionRequirement.Permission,
    title: Text,
    body: Text,
) {
    val args = UnscopedDestinations.PermissionRequirement.Args(permission, title, body)
    this.navigate(
        route = UnscopedDestinations.PermissionRequirement.routeSchema,
        args = UnscopedDestinations.PermissionRequirement.createArgsBundle(args),
    )
}
