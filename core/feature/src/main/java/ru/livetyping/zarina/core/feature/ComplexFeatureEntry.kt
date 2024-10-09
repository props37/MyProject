package ru.livetyping.zarina.core.feature

import androidx.navigation.NavGraphBuilder
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry

public interface ComplexFeatureEntry<NavEntry : NavigationEntry, NavActions : NavigationActions> :
    FeatureEntry<NavEntry, NavActions> {

    public fun NavGraphBuilder.navigation(actions: NavActions)
}
