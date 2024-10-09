package ru.livetyping.zarina.core.feature

import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry

public interface SingleFeatureEntry<NavEntry : NavigationEntry, NavActions : NavigationActions> :
    FeatureEntry<NavEntry, NavActions>
