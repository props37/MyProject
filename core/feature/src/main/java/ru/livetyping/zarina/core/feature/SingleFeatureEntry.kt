package ru.livetyping.zarina.core.feature

import androidx.navigation.NavGraphBuilder
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry

public interface SingleFeatureEntry<NavEntry : NavigationEntry, NavActions : NavigationActions> :
    FeatureEntry<NavEntry, NavActions> {

    public fun NavGraphBuilder.composable(actions: NavActions)
}
