package ru.livetyping.zarina.feature.signup.ui.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signup.ui.impl.phoneconfirmation.SignUpConfirmationNavActions
import ru.livetyping.zarina.feature.signup.ui.impl.phoneconfirmation.SignUpConfirmationNavEntry
import ru.livetyping.zarina.feature.signup.ui.impl.phoneconfirmation.SignUpConfirmationScreen

internal fun NavGraphBuilder.signUpConfirmationScreen(actions: SignUpConfirmationNavActions) {
    composable<SignUpConfirmationNavEntry> {
        SignUpConfirmationScreen(actions)
    }
}
