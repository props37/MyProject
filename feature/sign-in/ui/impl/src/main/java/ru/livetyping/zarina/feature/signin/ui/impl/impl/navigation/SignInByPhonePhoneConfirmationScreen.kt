package ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.SignInByPhonePhoneConfirmationNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.SignInByPhonePhoneConfirmationNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.SignInByPhonePhoneConfirmationScreen

internal fun NavGraphBuilder.signInByPhonePhoneConfirmationScreen(actions: SignInByPhonePhoneConfirmationNavActions) {
    composable<SignInByPhonePhoneConfirmationNavEntry> {
        SignInByPhonePhoneConfirmationScreen(actions)
    }
}
