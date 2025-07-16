package ru.livetyping.zarina.feature.profile.ui.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.storelist.StoreListNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.storelist.StoreListNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.storelist.StoreListScreen

internal fun NavGraphBuilder.storeListScreen(actions: StoreListNavActions) {
    composable<StoreListNavEntry> {
        StoreListScreen(actions)
    }
}
