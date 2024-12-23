package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.EmailChangingNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.EmailChangingNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.EmailChangingScreen

internal fun NavGraphBuilder.emailChangingScreen(actions: EmailChangingNavActions) {
    composable<EmailChangingNavEntry> {
        EmailChangingScreen(actions)
    }
}
