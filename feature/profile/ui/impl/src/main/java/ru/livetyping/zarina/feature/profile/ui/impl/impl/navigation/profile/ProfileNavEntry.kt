package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileScreen

@Serializable
internal data object ProfileNavEntry

internal fun NavGraphBuilder.profileScreen(actions: ProfileNavActions) {
    composable<ProfileNavEntry> {
        ProfileScreen(actions)
    }
}
