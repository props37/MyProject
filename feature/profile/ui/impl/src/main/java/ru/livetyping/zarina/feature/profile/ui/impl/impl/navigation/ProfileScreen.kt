package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileScreen

internal fun NavGraphBuilder.profileScreen(actions: ProfileNavActions) {
    composable<ProfileNavEntry> {
        ProfileScreen(actions)
    }
}
