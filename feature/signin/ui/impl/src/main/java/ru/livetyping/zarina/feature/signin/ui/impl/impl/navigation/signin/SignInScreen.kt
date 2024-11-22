package ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation.signin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.SignInNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.SignInNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.SignInScreen

internal fun NavGraphBuilder.signInScreen(actions: SignInNavActions) {
    composable<SignInNavEntry> {
        SignInScreen(actions)
    }
}
