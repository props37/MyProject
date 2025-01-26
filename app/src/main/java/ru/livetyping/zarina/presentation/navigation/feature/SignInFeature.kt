package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature
import ru.livetyping.zarina.feature.signin.ui.api.SignInNavActions
import ru.livetyping.zarina.feature.signup.ui.api.SignUpFeature

fun NavGraphBuilder.signInFeature(
    navController: NavHostController,
    feature: SignInFeature,
    actions: SignInNavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
        )
    }
}

@Composable
fun rememberSignInNavActions(
    navController: NavHostController
): SignInNavActions {
    return remember(navController) {
        SignInNavActions(
            onSignUpClicked = {
                navController.navigate(SignUpFeature.getNavEntry()) {
                    popUpTo(SignInFeature.getNavEntry()) { inclusive = true }
                }
            },
        )
    }
}
