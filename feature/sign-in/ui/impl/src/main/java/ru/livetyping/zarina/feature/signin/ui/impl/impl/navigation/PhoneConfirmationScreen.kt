package ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation.PhoneConfirmationNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation.PhoneConfirmationNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation.PhoneConfirmationScreen

internal fun NavGraphBuilder.phoneConfirmationScreen(actions: PhoneConfirmationNavActions) {
    composable<PhoneConfirmationNavEntry> {
        PhoneConfirmationScreen(actions)
    }
}
