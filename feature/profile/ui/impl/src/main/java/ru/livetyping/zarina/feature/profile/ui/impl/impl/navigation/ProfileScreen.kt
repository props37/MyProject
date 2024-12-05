package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.core.navigationutil.ScreenResultRetriever
import ru.livetyping.zarina.feature.profile.ui.ProfileSelectedCityResult
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileViewModel

internal fun NavGraphBuilder.profileScreen(
    actions: ProfileNavActions,
    selectedCityResultRetriever: ScreenResultRetriever<ProfileSelectedCityResult>
) {
    composable<ProfileNavEntry> { navBackStackEntry ->
        ProfileScreen(
            navActions = actions,
            viewModel = hiltViewModel { factory: ProfileViewModel.Factory ->
                val selectedCityResultFlow = selectedCityResultRetriever.get(navBackStackEntry)
                factory.create(selectedCityResultFlow)
            },
        )
    }
}
