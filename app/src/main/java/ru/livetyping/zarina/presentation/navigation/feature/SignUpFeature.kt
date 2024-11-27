package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.signup.ui.api.SignUpFeature
import ru.livetyping.zarina.feature.signup.ui.api.SignUpNavActions

fun NavGraphBuilder.signUpFeature(
    navController: NavHostController,
    feature: SignUpFeature,
    actions: SignUpNavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
        )
    }
}

@Composable
fun rememberSignUpNavActions(
    navController: NavHostController
): SignUpNavActions {
    return remember(navController) {
        SignUpNavActions()
    }
}
