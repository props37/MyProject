package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.PasswordChangingNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.PasswordChangingNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.PasswordChangingScreen

internal fun NavGraphBuilder.passwordChangingScreen(actions: PasswordChangingNavActions) {
    composable<PasswordChangingNavEntry> {
        PasswordChangingScreen(actions)
    }
}
