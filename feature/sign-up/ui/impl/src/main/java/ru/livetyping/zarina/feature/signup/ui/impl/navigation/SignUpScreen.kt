package ru.livetyping.zarina.feature.signup.ui.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signup.ui.impl.signup.SignUpNavActions
import ru.livetyping.zarina.feature.signup.ui.impl.signup.SignUpNavEntry
import ru.livetyping.zarina.feature.signup.ui.impl.signup.SignUpScreen

internal fun NavGraphBuilder.signUpScreen(actions: SignUpNavActions) {
    composable<SignUpNavEntry> {
        SignUpScreen(actions)
    }
}
