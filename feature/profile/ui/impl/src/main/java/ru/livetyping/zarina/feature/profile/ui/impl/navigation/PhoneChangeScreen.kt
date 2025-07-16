package ru.livetyping.zarina.feature.profile.ui.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.phonechange.PhoneChangeNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.phonechange.PhoneChangeNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.phonechange.PhoneChangeScreen

internal fun NavGraphBuilder.phoneChangeScreen(actions: PhoneChangeNavActions) {
    composable<PhoneChangeNavEntry> {
        PhoneChangeScreen(actions)
    }
}
