package ru.livetyping.zarina.feature.signup.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.SignUpNavActions
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.SignUpNavEntry
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.SignUpScreen

internal fun NavGraphBuilder.signUpScreen(actions: SignUpNavActions) {
    composable<SignUpNavEntry> {
        SignUpScreen(actions)
    }
}
