package ru.livetyping.zarina.feature.home.ui.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.home.ui.HomeFeatureEntry
import ru.livetyping.zarina.feature.home.ui.HomeNavActions
import ru.livetyping.zarina.feature.home.ui.HomeNavEntry
import ru.livetyping.zarina.feature.home.ui.impl.impl.HomeScreen

public class HomeFeatureEntryImpl : HomeFeatureEntry {
    override fun NavGraphBuilder.composable(actions: HomeNavActions) {
        composable<HomeNavEntry> {
            HomeScreen(navActions = actions)
        }
    }
}
