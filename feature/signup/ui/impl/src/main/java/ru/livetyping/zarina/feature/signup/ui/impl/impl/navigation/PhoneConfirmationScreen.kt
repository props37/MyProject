package ru.livetyping.zarina.feature.signup.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signup.ui.impl.impl.otp.PhoneConfirmationNavActions
import ru.livetyping.zarina.feature.signup.ui.impl.impl.otp.PhoneConfirmationNavEntry
import ru.livetyping.zarina.feature.signup.ui.impl.impl.otp.PhoneConfirmationScreen

internal fun NavGraphBuilder.phoneConfirmationScreen(actions: PhoneConfirmationNavActions) {
    composable<PhoneConfirmationNavEntry> {
        PhoneConfirmationScreen(actions)
    }
}
