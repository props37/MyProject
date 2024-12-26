package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.PhoneChangeNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.PhoneChangeNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.PhoneChangeScreen

internal fun NavGraphBuilder.phoneChangeScreen(actions: PhoneChangeNavActions) {
    composable<PhoneChangeNavEntry> {
        PhoneChangeScreen(actions)
    }
}
