package ru.livetyping.zarina.feature.profile.ui.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.profiledetails.ProfileDetailsNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.profiledetails.ProfileDetailsNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.profiledetails.ProfileDetailsScreen

internal fun NavGraphBuilder.profileDetailsScreen(actions: ProfileDetailsNavActions) {
    composable<ProfileDetailsNavEntry> {
        ProfileDetailsScreen(actions)
    }
}
