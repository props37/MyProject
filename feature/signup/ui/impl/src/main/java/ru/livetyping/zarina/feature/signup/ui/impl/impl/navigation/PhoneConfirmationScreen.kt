package ru.livetyping.zarina.feature.signup.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation.SignUpConfirmationNavActions
import ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation.SignUpConfirmationNavEntry
import ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation.SignUpConfirmationScreen

internal fun NavGraphBuilder.signUpConfirmationScreen(actions: SignUpConfirmationNavActions) {
    composable<SignUpConfirmationNavEntry> {
        SignUpConfirmationScreen(actions)
    }
}
