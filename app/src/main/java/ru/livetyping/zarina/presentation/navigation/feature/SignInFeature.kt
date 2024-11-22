package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature
import ru.livetyping.zarina.feature.signin.ui.api.SignInNavActions

fun NavGraphBuilder.signInFeature(
    navController: NavHostController,
    feature: SignInFeature,
    actions: SignInNavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
        )
    }
}

@Composable
fun rememberSignInNavActions(
    navController: NavHostController
): SignInNavActions {
    return remember(navController) {
        SignInNavActions()
    }
}
