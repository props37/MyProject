package ru.livetyping.zarina.feature.signin.ui.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signin.ui.impl.signinbyphone.SignInByPhoneConfirmationNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.signinbyphone.SignInByPhoneConfirmationNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.signinbyphone.SignInByPhoneConfirmationScreen

internal fun NavGraphBuilder.signInByPhonePhoneConfirmationScreen(actions: SignInByPhoneConfirmationNavActions) {
    composable<SignInByPhoneConfirmationNavEntry> {
        SignInByPhoneConfirmationScreen(actions)
    }
}
