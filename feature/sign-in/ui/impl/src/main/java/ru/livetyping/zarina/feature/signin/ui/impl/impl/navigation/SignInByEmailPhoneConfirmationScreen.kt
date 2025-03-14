package ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.SignInByEmailConfirmationNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.SignInByEmailConfirmationNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.SignInByEmailConfirmationScreen

internal fun NavGraphBuilder.signInByEmailPhoneConfirmationScreen(actions: SignInByEmailConfirmationNavActions) {
    composable<SignInByEmailConfirmationNavEntry> {
        SignInByEmailConfirmationScreen(actions)
    }
}
