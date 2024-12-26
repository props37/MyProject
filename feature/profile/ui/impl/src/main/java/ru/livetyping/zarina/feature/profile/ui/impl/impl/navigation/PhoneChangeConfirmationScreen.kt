package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.PhoneChangeConfirmationNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.PhoneChangeConfirmationNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.PhoneChangeConfirmationScreen

internal fun NavGraphBuilder.phoneChangeConfirmationScreen(
    actions: PhoneChangeConfirmationNavActions,
) {
    composable<PhoneChangeConfirmationNavEntry> {
        PhoneChangeConfirmationScreen(actions)
    }
}
