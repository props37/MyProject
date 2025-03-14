package ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.PhoneConfirmationNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.PhoneConfirmationNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.PhoneConfirmationScreen

internal fun NavGraphBuilder.phoneConfirmationScreen(actions: PhoneConfirmationNavActions) {
    composable<PhoneConfirmationNavEntry> {
        PhoneConfirmationScreen(actions)
    }
}
