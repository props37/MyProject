package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.navigation.base.bottomSheetDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.generic.bottomsheet.GenericBottomSheetScreen
import ru.livetyping.zarina.presentation.screen.generic.bottomsheet.GenericBottomSheetScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.genericBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.GenericBottomSheet) {
        GenericBottomSheetScreen(
            navigate = { action ->
                when (action) {
                    GenericBottomSheetScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.GenericBottomSheet.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToGenericBottomSheetScreen(title: Text, body: Text) {
    val args = UnscopedDestinations.GenericBottomSheet.Args(title, body)
    this.navigate(
        route = UnscopedDestinations.GenericBottomSheet.routeSchema,
        args = UnscopedDestinations.GenericBottomSheet.createArgsBundle(args),
    )
}
