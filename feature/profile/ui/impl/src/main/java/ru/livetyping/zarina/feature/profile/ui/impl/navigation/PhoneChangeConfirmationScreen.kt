package ru.livetyping.zarina.feature.profile.ui.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.phonechangeconfirmation.PhoneChangeConfirmationNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.phonechangeconfirmation.PhoneChangeConfirmationNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.phonechangeconfirmation.PhoneChangeConfirmationScreen

internal fun NavGraphBuilder.phoneChangeConfirmationScreen(
    actions: PhoneChangeConfirmationNavActions,
) {
    composable<PhoneChangeConfirmationNavEntry> {
        PhoneChangeConfirmationScreen(actions)
    }
}
