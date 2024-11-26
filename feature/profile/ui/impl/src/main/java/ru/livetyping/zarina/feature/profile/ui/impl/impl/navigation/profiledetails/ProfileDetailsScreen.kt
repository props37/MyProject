package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.profiledetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.ProfileDetailsNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.ProfileDetailsNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.ProfileDetailsScreen

internal fun NavGraphBuilder.profileDetailsScreen(actions: ProfileDetailsNavActions) {
    composable<ProfileDetailsNavEntry> {
        ProfileDetailsScreen(actions)
    }
}
