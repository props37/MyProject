package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.EmailChangeNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.EmailChangeNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.EmailChangeScreen

internal fun NavGraphBuilder.emailChangeScreen(actions: EmailChangeNavActions) {
    composable<EmailChangeNavEntry> {
        EmailChangeScreen(actions)
    }
}
