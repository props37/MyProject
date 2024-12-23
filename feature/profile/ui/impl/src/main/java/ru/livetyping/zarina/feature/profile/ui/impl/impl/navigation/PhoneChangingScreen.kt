package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.PhoneChangingNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.PhoneChangingNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.PhoneChangingScreen

internal fun NavGraphBuilder.phoneChangingScreen(actions: PhoneChangingNavActions) {
    composable<PhoneChangingNavEntry> {
        PhoneChangingScreen(actions)
    }
}
