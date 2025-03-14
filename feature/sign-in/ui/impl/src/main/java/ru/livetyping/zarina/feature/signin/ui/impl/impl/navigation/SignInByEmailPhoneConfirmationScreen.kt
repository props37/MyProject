package ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.SignInByEmailPhoneConfirmationNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.SignInByEmailPhoneConfirmationNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.SignInByEmailPhoneConfirmationScreen

internal fun NavGraphBuilder.signInByEmailPhoneConfirmationScreen(actions: SignInByEmailPhoneConfirmationNavActions) {
    composable<SignInByEmailPhoneConfirmationNavEntry> {
        SignInByEmailPhoneConfirmationScreen(actions)
    }
}
