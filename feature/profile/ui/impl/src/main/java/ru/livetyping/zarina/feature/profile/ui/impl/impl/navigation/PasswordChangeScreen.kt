package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchange.PasswordChangeNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchange.PasswordChangeNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchange.PasswordChangeScreen

internal fun NavGraphBuilder.passwordChangeScreen(actions: PasswordChangeNavActions) {
    composable<PasswordChangeNavEntry> {
        PasswordChangeScreen(actions)
    }
}
